package com.example.budgetingapp.services.impl;

import static com.example.budgetingapp.constants.Constants.NO_ACCOUNT;

import com.example.budgetingapp.dtos.budget.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budget.response.BudgetResponseDto;
import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.entities.Budget;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.exceptions.AlreadyExistsException;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.BudgetMapper;
import com.example.budgetingapp.repositories.budget.BudgetRepository;
import com.example.budgetingapp.repositories.categories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.ExpenseSpecificationBuilder;
import com.example.budgetingapp.services.BudgetService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final BudgetMapper budgetMapper;
    private final ExpenseSpecificationBuilder expenseSpecificationBuilder;

    @Override
    public BudgetResponseDto getMainBudgetByUserId(Long userId) {
        //TODO
        return budgetMapper
                .toBudgetDto(budgetRepository
                        .findByIsTopLevelBudget(true)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No default budget found for user with id " + userId)));
    }

    @Override
    public BudgetResponseDto saveBudget(Long userId, BudgetRequestDto budgetRequestDto) {
        if (budgetRepository.existByNameAndUserId(budgetRequestDto.name(), userId)) {
            throw new AlreadyExistsException("Budget with name " + budgetRequestDto.name()
                    + " for user " + userId + " already exists");
        }
        isCategoryPresentInDb(userId, budgetRequestDto);
        Budget newBudget = budgetMapper.toBudget(budgetRequestDto);
        return budgetMapper.toBudgetDto(budgetRepository.save(newBudget));
    }

    @Override
    public BudgetResponseDto updateBudget(Long userId, BudgetRequestDto budgetRequestDto) {
        //TODO
        return null;
    }

    @Override
    public List<BudgetResponseDto> getAllBudgets(Long userId) {
        budgetRepository.findAllByUserId(userId).forEach(budget -> {
            String[] categoryIds = parseCategoryIdsFromBudget(budget);
            FilterTransactionsDto filterTransactionsDto =
                    initializeFilterWithNoAccount(budget, categoryIds);
            Specification<Expense> expenseSpecification =
                    expenseSpecificationBuilder.build(filterTransactionsDto);
            List<Expense> expenses = expenseRepository.findAll(expenseSpecification);
            List<BigDecimal> expensesAmounts = expenses.stream()
                    .map(Expense::getAmount)
                    .toList();
            budget.setCurrentSum(getSumOfExpenses(expensesAmounts));
            if (isExceeded(budget) < 0) {
                budget.setExceeded(true);
            }
            budgetRepository.save(budget);
        });
        return budgetRepository
                .findAllByUserId(userId)
                .stream()
                .map(budgetMapper::toBudgetDto)
                .toList();
    }

    @Override
    public void deleteBudgetById(Long userId, Long budgetId) {
        Budget topLevelBudget = budgetRepository
                .findByIsTopLevelBudget(true)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No default budget found for user with id " + userId));
        if (topLevelBudget.getId().equals(budgetId)) {
            throw new IllegalArgumentException("You can't delete top level budget!"
                    + " Consider updating it instead");
        }
        if (budgetRepository.existByIdAndUserId(budgetId, userId)) {
            budgetRepository.deleteByIdAndUserId(budgetId, budgetId);
        } else {
            throw new EntityNotFoundException("No budget found with id " + budgetId
                    + " for user with id " + userId);
        }
    }

    private void isCategoryPresentInDb(Long userId, BudgetRequestDto budgetRequestDto) {
        for (Long categoryId : budgetRequestDto.categoryIds()) {
            if (!expenseCategoryRepository.existsByIdAndUserId(userId, categoryId)) {
                throw new EntityNotFoundException("No category with id " + categoryId
                        + " was found for user with id " + userId);
            }
        }
    }

    private String[] parseCategoryIdsFromBudget(Budget budget) {
        return budget.getExpenseCategories().stream()
                .map(ExpenseCategory::getId)
                .map(String::valueOf)
                .toArray(String[]::new);
    }

    private FilterTransactionsDto initializeFilterWithNoAccount(
            Budget budget, String[] categoryIds) {
        return new FilterTransactionsDto(
                NO_ACCOUNT,
                budget.getFromDate().toString(),
                budget.getToDate().toString(),
                categoryIds);
    }

    private BigDecimal getSumOfExpenses(List<BigDecimal> expensesAmounts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal expenseAmount : expensesAmounts) {
            sum = sum.add(expenseAmount);
        }
        return sum;
    }

    private int isExceeded(Budget budget) {
        return (budget.getLimit().compareTo(budget.getCurrentSum()));
    }
}
