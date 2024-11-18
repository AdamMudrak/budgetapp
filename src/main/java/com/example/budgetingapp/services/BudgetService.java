package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budgets.response.BudgetResponseDto;
import java.util.List;

public interface BudgetService {

    BudgetResponseDto saveBudget(Long userId, BudgetRequestDto budgetRequestDto);

    List<BudgetResponseDto> updateAndGetAllBudgets(Long userId);

    void deleteBudgetById(Long userId, Long budgetId);
}
