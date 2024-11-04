package com.example.budgetingapp.controllers;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_201;
import static com.example.budgetingapp.constants.Constants.CODE_204;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.ADD_TARGET;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.ADD_TARGET_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.DESTROY_TARGET;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.DESTROY_TARGET_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.GET_ALL_TARGETS;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.GET_ALL_TARGETS_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.REPLENISH_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.REPLENISH_TARGET;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.SUCCESSFULLY_ADDED_TARGET;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.SUCCESSFULLY_DESTROYED_TARGET;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.SUCCESSFULLY_REPLENISHED_TARGET;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.SUCCESSFULLY_RETRIEVED_TARGETS;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.TARGETS;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.TARGET_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.TARGET_API_NAME;

import com.example.budgetingapp.dtos.targets.request.DeleteTargetRequestDto;
import com.example.budgetingapp.dtos.targets.request.ReplenishTargetRequestDto;
import com.example.budgetingapp.dtos.targets.request.TargetTransactionRequestDto;
import com.example.budgetingapp.dtos.targets.response.TargetTransactionResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.TargetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = TARGET_API_NAME, description = TARGET_API_DESCRIPTION)
@RequestMapping(TARGETS)
@RequiredArgsConstructor
public class TargetController {
    private final TargetService targetService;

    @Operation(summary = ADD_TARGET_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description = SUCCESSFULLY_ADDED_TARGET)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(ADD_TARGET)
    @ResponseStatus(HttpStatus.CREATED)
    public TargetTransactionResponseDto addTarget(@AuthenticationPrincipal User user,
                                      @Valid @RequestBody TargetTransactionRequestDto requestDto) {
        return targetService.saveTarget(user.getId(), requestDto);
    }

    @Operation(summary = REPLENISH_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_REPLENISHED_TARGET)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(REPLENISH_TARGET)
    public TargetTransactionResponseDto replenishTarget(@AuthenticationPrincipal User user,
                                        @Valid @RequestBody ReplenishTargetRequestDto requestDto) {
        return targetService.replenishTarget(user.getId(), requestDto);
    }

    @Operation(summary = GET_ALL_TARGETS_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_RETRIEVED_TARGETS)
    @GetMapping(GET_ALL_TARGETS)
    public List<TargetTransactionResponseDto> getAllTargets(@AuthenticationPrincipal User user,
                                                            Pageable pageable) {
        return targetService.getAllTargets(user.getId(), pageable);
    }

    @Operation(summary = DESTROY_TARGET_SUMMARY)
    @ApiResponse(responseCode = CODE_204, description = SUCCESSFULLY_DESTROYED_TARGET)
    @DeleteMapping(DESTROY_TARGET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTarget(@AuthenticationPrincipal User user,
                             @Valid @RequestBody DeleteTargetRequestDto requestDto) {
        targetService.deleteByTargetId(user.getId(), requestDto);
    }
}
