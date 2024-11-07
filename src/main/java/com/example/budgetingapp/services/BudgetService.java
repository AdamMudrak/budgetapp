package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budgets.response.BudgetResponseDto;
import com.example.budgetingapp.dtos.budgets.response.TopLevelBudgetResponseDto;
import java.util.List;

public interface BudgetService {
    TopLevelBudgetResponseDto updateAndGetMainBudgetByUserId(Long userId);

    BudgetResponseDto saveBudget(Long userId, BudgetRequestDto budgetRequestDto);

    List<BudgetResponseDto> updateAndGetAllBudgetsWithoutTopLevel(Long userId);

    void deleteBudgetById(Long userId, Long budgetId);
}
