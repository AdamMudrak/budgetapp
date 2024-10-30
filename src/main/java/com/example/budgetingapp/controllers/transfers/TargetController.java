package com.example.budgetingapp.controllers.transfers;

import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.TARGETS;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.TARGET_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.TargetControllerConstants.TARGET_API_NAME;

import com.example.budgetingapp.dtos.transfers.request.DeleteTargetRequestDto;
import com.example.budgetingapp.dtos.transfers.request.ReplenishTargetRequestDto;
import com.example.budgetingapp.dtos.transfers.request.TargetTransactionRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TargetTransactionResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.TargetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = TARGET_API_NAME, description = TARGET_API_DESCRIPTION)
@RequestMapping(TARGETS)
@RequiredArgsConstructor
public class TargetController {
    private final TargetService targetService;

    @PostMapping
    public TargetTransactionResponseDto addTarget(@AuthenticationPrincipal User user,
                                              @RequestBody TargetTransactionRequestDto requestDto) {
        return targetService.saveTarget(user.getId(), requestDto);
    }

    @PostMapping("/replenish")
    public TargetTransactionResponseDto replenishTarget(@AuthenticationPrincipal User user,
                                          @RequestBody ReplenishTargetRequestDto requestDto) {
        return targetService.replenishTarget(user.getId(), requestDto);
    }

    @DeleteMapping
    public void deleteTarget(@AuthenticationPrincipal User user,
                                @RequestBody DeleteTargetRequestDto requestDto) {
        targetService.deleteByTargetId(user.getId(), requestDto);
    }

    @GetMapping
    public List<TargetTransactionResponseDto> getAllTargets(@AuthenticationPrincipal User user,
                                                            Pageable pageable) {
        return targetService.getAllTargets(user.getId(), pageable);
    }
}
