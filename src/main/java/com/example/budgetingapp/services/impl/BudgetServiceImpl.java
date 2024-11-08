package com.example.budgetingapp.services.impl;

import static com.example.budgetingapp.constants.Constants.NO_ACCOUNT;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.BUDGET_QUANTITY_THRESHOLD;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_MONTH_STEP;

import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budgets.response.BudgetResponseDto;
import com.example.budgetingapp.dtos.budgets.response.TopLevelBudgetResponseDto;
import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.entities.Budget;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
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
import java.util.List;
import java.util.Set;
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
    private final UserRepository userRepository;

    @Override
    public TopLevelBudgetResponseDto updateAndGetMainBudgetByUserId(Long userId) {
        Budget topLevelBudget = budgetRepository.findByUserIdAndIsTopLevelBudget(userId, true)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No default budget found for user with id " + userId));
        resetTopLevelBudgetBeforeRenewal(topLevelBudget);
        updateBudgetsBeforeRetrieval(userId);
        budgetRepository.findAllByUserId(userId)
                .forEach(budget -> {
                    if (!budget.getId().equals(topLevelBudget.getId())) {
                        if (!topLevelBudget.getExpenseCategories()
                                .containsAll(budget.getExpenseCategories())) {
                            topLevelBudget.getExpenseCategories()
                                    .addAll(budget.getExpenseCategories());
                        }
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
                        budgetRepository.save(topLevelBudget);
                    }
                });
        return budgetMapper.toTopLevelBudgetDto(budgetRepository.findByUserIdAndIsTopLevelBudget(
                userId, true).orElseThrow(() -> new EntityNotFoundException(
                "No default budget found for user with id " + userId)));
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
    public List<BudgetResponseDto> updateAndGetAllBudgetsWithoutTopLevel(Long userId) {
        updateBudgetsBeforeRetrieval(userId);
        return budgetRepository
                .findAllByUserIdAndIsTopLevelBudget(userId, false)
                .stream()
                .map(budgetMapper::toBudgetDto)
                .toList();
    }

    @Override
    public void deleteBudgetById(Long userId, Long budgetId) {
        Budget topLevelBudget = budgetRepository
                .findByUserIdAndIsTopLevelBudget(userId, true)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No default budget found for user with id " + userId));
        if (topLevelBudget.getId().equals(budgetId)) {
            throw new IllegalArgumentException("You can't delete top level budget!"
                    + " Consider deleting subsidiary it instead");
        }
        Budget budgetToBeDeleted = budgetRepository.findByIdAndUserId(budgetId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No budget with id " + budgetId + " was found for user with id " + userId));
        Set<ExpenseCategory> topLevelBudgetExpenseCategories =
                topLevelBudget.getExpenseCategories();
        topLevelBudgetExpenseCategories.removeAll(budgetToBeDeleted.getExpenseCategories());
        budgetRepository.deleteByIdAndUserId(budgetId, userId);
        budgetRepository.save(topLevelBudget);
    }

    private void isCategoryPresentInDb(Long userId, BudgetRequestDto budgetRequestDto) {
        if (!expenseCategoryRepository.existsByIdAndUserId(budgetRequestDto.categoryId(), userId)) {
            throw new EntityNotFoundException("No category with id " + budgetRequestDto.categoryId()
                    + " was found for user with id " + userId);
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
        return (budget.getLimitSum().compareTo(budget.getCurrentSum()));
    }

    private void updateBudgetsBeforeRetrieval(Long userId) {
        Budget topLevelBudget = budgetRepository.findByUserIdAndIsTopLevelBudget(userId, true)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No default budget found for user with id " + userId));
        Long topLevelBudgetId = topLevelBudget.getId();
        budgetRepository.findAllByUserId(userId).forEach(budget -> {
            if (!budget.getId().equals(topLevelBudgetId)) {
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
            }
        });
    }

    private void checkIfThereAreBudgetsWithSameCategories(Long userId,
                                                          BudgetRequestDto budgetRequestDto) {
        if (budgetRepository.existsByExpenseCategoryIdAndUserId(budgetRequestDto.categoryId(),
                userId)) {
            throw new AlreadyExistsException("You can't create a budget containing category "
                    + budgetRequestDto.categoryId() + " as it is already used in another budget");
        }
    }

    private void resetTopLevelBudgetBeforeRenewal(Budget topLevelBudget) {
        topLevelBudget.setFromDate(LocalDate.now());
        topLevelBudget.setToDate(LocalDate.now().plusMonths(DEFAULT_MONTH_STEP));
        topLevelBudget.getExpenseCategories().clear();
        topLevelBudget.setLimitSum(BigDecimal.ZERO);
        topLevelBudget.setCurrentSum(BigDecimal.ZERO);
        topLevelBudget.setExceeded(false);
    }
}
