package com.example.budgetingapp.controllers;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_201;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.controllers.BudgetControllerConstants.ADD_BUDGET_SUMMARY;
import static com.example.budgetingapp.constants.controllers.BudgetControllerConstants.BUDGET_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.BudgetControllerConstants.BUDGET_API_NAME;
import static com.example.budgetingapp.constants.controllers.BudgetControllerConstants.DELETE_BUDGET_BY_ID_SUMMARY;
import static com.example.budgetingapp.constants.controllers.BudgetControllerConstants.GET_ALL_BUDGETS_SUMMARY;
import static com.example.budgetingapp.constants.controllers.BudgetControllerConstants.SUCCESSFULLY_ADDED;
import static com.example.budgetingapp.constants.controllers.BudgetControllerConstants.SUCCESSFULLY_DELETED;
import static com.example.budgetingapp.constants.controllers.BudgetControllerConstants.SUCCESSFULLY_RETRIEVED;

import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budgets.response.BudgetResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize(ROLE_USER)
@RequiredArgsConstructor
@Tag(name = BUDGET_API_NAME, description = BUDGET_API_DESCRIPTION)
@RestController
@RequestMapping("/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    @Operation(summary = ADD_BUDGET_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description = SUCCESSFULLY_ADDED)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping("/add-budget")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponseDto addBudget(@AuthenticationPrincipal User user,
                                       @Valid @RequestBody BudgetRequestDto budgetRequestDto) {
        return budgetService.saveBudget(user.getId(), budgetRequestDto);
    }

    @Operation(summary = GET_ALL_BUDGETS_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_RETRIEVED)
    @GetMapping("/get-all-budgets")
    public List<BudgetResponseDto> getAllBudgets(@AuthenticationPrincipal User user) {
        return budgetService.updateAndGetAllBudgets(user.getId());
    }

    @Operation(summary = DELETE_BUDGET_BY_ID_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description = SUCCESSFULLY_DELETED)
    @DeleteMapping("/delete-budget/{budgetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudget(@AuthenticationPrincipal User user,
                                          @PathVariable Long budgetId) {
        budgetService.deleteBudgetById(user.getId(), budgetId);
    }
}
