package com.example.budgetingapp.services.impl;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.BUDGET_QUANTITY_THRESHOLD;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_BUDGET_NAME;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_MONTH_STEP;

import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budgets.response.BudgetResponseDto;
import com.example.budgetingapp.dtos.budgets.response.TopLevelBudgetResponseDto;
import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.entities.Budget;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.exceptions.conflictexpections.AlreadyExistsException;
import com.example.budgetingapp.exceptions.conflictexpections.ConflictException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.BudgetMapper;
import com.example.budgetingapp.repositories.budget.BudgetRepository;
import com.example.budgetingapp.repositories.categories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.ExpenseSpecificationBuilder;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.BudgetService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final BudgetMapper budgetMapper;
    private final ExpenseSpecificationBuilder expenseSpecificationBuilder;
    private final UserRepository userRepository;

    @Override
    public TopLevelBudgetResponseDto setAndGetTopLevelBudgetByUserId(Long userId) {
        TopLevelBudgetResponseDto topLevelBudget = getEmptyTopLevelBudget();
        updateBudgetsBeforeRetrieval(userId);
        budgetRepository.findAllByUserId(userId)
                .forEach(budget -> {
                    topLevelBudget.getCategoryIds()
                            .add(budget.getExpenseCategory().getId());
                    if (topLevelBudget.getFromDate().isAfter(budget.getFromDate())) {
                        topLevelBudget.setFromDate(budget.getFromDate());
                    }
                    if (topLevelBudget.getToDate().isBefore(budget.getToDate())) {
                        topLevelBudget.setToDate(budget.getToDate());
                    }
                    topLevelBudget.setLimitSum(topLevelBudget.getLimitSum()
                            .add(budget.getLimitSum()));
                    topLevelBudget.setCurrentSum(topLevelBudget.getCurrentSum()
                            .add(budget.getCurrentSum()));
                    topLevelBudget.setExceeded(isExceeded(topLevelBudget) < 0);
                });
        return topLevelBudget;
    }

    @Override
    public BudgetResponseDto saveBudget(Long userId, BudgetRequestDto budgetRequestDto) {
        if (budgetRepository.countBudgetsByUserId(userId) >= BUDGET_QUANTITY_THRESHOLD) {
            throw new ConflictException("You can't have more than " + BUDGET_QUANTITY_THRESHOLD
                    + " budgets!");
        }

        if (budgetRepository.existsByNameAndUserId(budgetRequestDto.name(), userId)) {
            throw new AlreadyExistsException("Budget with name " + budgetRequestDto.name()
                    + " for user " + userId + " already exists");
        }
        isCategoryPresentInDb(userId, budgetRequestDto);
        checkIfThereAreBudgetsWithSameCategories(userId, budgetRequestDto);
        Budget newBudget = budgetMapper.toBudget(budgetRequestDto);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        newBudget.setUser(currentUser);
        return budgetMapper.toBudgetDto(budgetRepository.save(newBudget));
    }

    @Override
    public List<BudgetResponseDto> updateAndGetAllBudgets(Long userId) {
        updateBudgetsBeforeRetrieval(userId);
        return budgetRepository
                .findAllByUserId(userId)
                .stream()
                .map(budgetMapper::toBudgetDto)
                .toList();
    }

    @Override
    public void deleteBudgetById(Long userId, Long budgetId) {
        if (budgetRepository.existsByIdAndUserId(budgetId, userId)) {
            budgetRepository.deleteByIdAndUserId(budgetId, userId);
        } else {
            throw new EntityNotFoundException(
                    "No budget with id " + budgetId + " was found for user with id " + userId);
        }
    }

    private TopLevelBudgetResponseDto getEmptyTopLevelBudget() {
        TopLevelBudgetResponseDto topLevelBudget = new TopLevelBudgetResponseDto();
        topLevelBudget.setName(DEFAULT_BUDGET_NAME);
        topLevelBudget.setFromDate(LocalDate.now());
        topLevelBudget.setToDate(LocalDate.now().plusMonths(DEFAULT_MONTH_STEP));
        topLevelBudget.setCategoryIds(new HashSet<>());
        topLevelBudget.setLimitSum(BigDecimal.ZERO);
        topLevelBudget.setCurrentSum(BigDecimal.ZERO);
        topLevelBudget.setExceeded(false);
        return topLevelBudget;
    }

    private void updateBudgetsBeforeRetrieval(Long userId) {
        budgetRepository.findAllByUserId(userId).forEach(budget -> {
            List<Expense> expenses = expenseRepository.findAll(
                    expenseSpecificationBuilder.build(
                            new FilterTransactionsDto(
                                    null,
                                    Set.of(budget.getExpenseCategory().getId()),
                                    budget.getFromDate().toString(),
                                    budget.getToDate().toString())));
            BigDecimal expensesAmount = expenses.stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            budget.setCurrentSum(expensesAmount);
            if (isExceeded(budget) < 0) {
                budget.setExceeded(true);
            }
            budgetRepository.save(budget);
        });
    }

    private int isExceeded(Budget budget) {
        return (budget.getLimitSum().compareTo(budget.getCurrentSum()));
    }

    private int isExceeded(TopLevelBudgetResponseDto budget) {
        return (budget.getLimitSum().compareTo(budget.getCurrentSum()));
    }

    private void isCategoryPresentInDb(Long userId, BudgetRequestDto budgetRequestDto) {
        if (!expenseCategoryRepository.existsByIdAndUserId(budgetRequestDto.categoryId(), userId)) {
            throw new EntityNotFoundException("No category with id " + budgetRequestDto.categoryId()
                    + " was found for user with id " + userId);
        }
    }

    private void checkIfThereAreBudgetsWithSameCategories(Long userId,
                                                          BudgetRequestDto budgetRequestDto) {
        if (budgetRepository.existsByExpenseCategoryIdAndUserId(budgetRequestDto.categoryId(),
                userId)) {
            throw new AlreadyExistsException("You can't create a budget containing category "
                    + budgetRequestDto.categoryId() + " as it is already used in another budget");
        }
    }
}
