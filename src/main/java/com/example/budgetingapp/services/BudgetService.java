package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budgets.response.BudgetResponseDto;
import java.util.List;

public interface BudgetService {
    BudgetResponseDto getMainBudgetByUserId(Long userId);

    BudgetResponseDto saveBudget(Long userId, BudgetRequestDto budgetRequestDto);

    BudgetResponseDto updateBudget(Long userId, BudgetRequestDto budgetRequestDto);

    List<BudgetResponseDto> getAllBudgets(Long userId);

    void deleteBudgetById(Long userId, Long budgetId);
}
