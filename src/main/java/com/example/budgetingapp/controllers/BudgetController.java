package com.example.budgetingapp.controllers;

import static com.example.budgetingapp.constants.Constants.ROLE_USER;

import com.example.budgetingapp.dtos.budget.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budget.response.BudgetResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.BudgetService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize(ROLE_USER)
@RequiredArgsConstructor
@RestController
@RequestMapping("/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping("add-budget")
    public BudgetResponseDto addBudget(@AuthenticationPrincipal User user,
                                       @RequestBody BudgetRequestDto budgetRequestDto) {
        return budgetService.saveBudget(user.getId(), budgetRequestDto);
    }

    @GetMapping("get-all-budgets")
    public List<BudgetResponseDto> getAllBudgets(@AuthenticationPrincipal User user) {
        return budgetService.getAllBudgets(user.getId());
    }

    @DeleteMapping("delete-budget")
    public void deleteBudget(@AuthenticationPrincipal User user,
                                          @PathVariable Long budgetId) {
        budgetService.deleteBudgetById(user.getId(), budgetId);
    }
}
