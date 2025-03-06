package com.example.budgetingapp.services.implementations;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.BUDGET_QUANTITY_THRESHOLD;

import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budgets.response.BudgetResponseDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.entities.Budget;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.exceptions.conflictexpections.AlreadyExistsException;
import com.example.budgetingapp.exceptions.conflictexpections.ConflictException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.BudgetMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.budget.BudgetRepository;
import com.example.budgetingapp.repositories.categories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.ExpenseSpecificationBuilder;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.interfaces.BudgetService;
import java.math.BigDecimal;
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
    private final AccountRepository accountRepository;

    @Override
    public BudgetResponseDto saveBudget(Long userId, BudgetRequestDto budgetRequestDto) {
        isAccountPresentByCurrencyAndUserId(userId, budgetRequestDto);
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
        return budgetMapper.toBudgetDtoList(budgetRepository
                        .findAllByUserId(userId));
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

    private void updateBudgetsBeforeRetrieval(Long userId) {
        budgetRepository.findAllByUserId(userId).forEach(budget -> {
            List<Expense> expenses = expenseRepository.findAllByUserIdUnpaged(userId,
                    expenseSpecificationBuilder.build(getFilterDtoWithNoAccount(budget)))
                    .stream()
                    .filter(expense -> expense.getCurrency().equals(budget.getCurrency()))
                    .toList();
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

    private FilterTransactionByDaysDto getFilterDtoWithNoAccount(Budget budget) {
        return new FilterTransactionByDaysDto(
                null,
                Set.of(budget.getExpenseCategory().getId()),
                budget.getFromDate().toString(),
                budget.getToDate().toString());
    }

    private void isAccountPresentByCurrencyAndUserId(Long userId,
                                                     BudgetRequestDto budgetRequestDto) {
        if (!accountRepository.existsByUserIdAndCurrency(userId, budgetRequestDto.currency())) {
            throw new EntityNotFoundException("No account with currency "
                    + budgetRequestDto.currency() + " was found for user with id " + userId);
        }
    }
}
