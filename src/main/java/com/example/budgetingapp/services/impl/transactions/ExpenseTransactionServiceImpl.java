package com.example.budgetingapp.services.impl.transactions;

import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.EXPENSE;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.TARGET_EXPENSE_CATEGORY;

import com.example.budgetingapp.dtos.transactions.request.CreateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByMonthsYearsDto;
import com.example.budgetingapp.dtos.transactions.response.GetTransactionsPageDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseDto;
import com.example.budgetingapp.dtos.transactions.response.charts.SumsByPeriodDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.exceptions.ConflictException;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.TransactionFailedException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.AccountRepository;
import com.example.budgetingapp.repositories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.UserRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.ExpenseSpecificationBuilder;
import com.example.budgetingapp.services.TransactionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Qualifier(EXPENSE)
@RequiredArgsConstructor
@Transactional
public class ExpenseTransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseSpecificationBuilder expenseSpecificationBuilder;
    private final TransactionsCommonFunctionsUtil transactionsCommonFunctionsUtil;

    @Override
    public SaveAndUpdateResponseDto saveTransaction(Long userId,
                                                    CreateTransactionDto requestTransactionDto) {
        isCategoryPresentInDb(userId, requestTransactionDto.categoryId());
        Account account = accountRepository
                .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id "
                                + requestTransactionDto.accountId() + " was found"
                                + " for user with id " + userId));
        if (transactionsCommonFunctionsUtil
                .isSufficientAmount(account, requestTransactionDto.amount()) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        account.setBalance(account.getBalance().subtract(requestTransactionDto.amount()));
        accountRepository.save(account);
        Expense expense = transactionMapper.toExpense(requestTransactionDto);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        expense.setUser(currentUser);
        expense.setCurrency(account.getCurrency());
        expenseRepository.save(expense);
        return transactionMapper.toPersistExpenseDto(expense);
    }

    @Override
    public GetTransactionsPageDto getAllTransactions(Long userId,
                                                              FilterTransactionByDaysDto filterDto,
                                                              Pageable pageable) {
        presenceCheck(userId, filterDto);
        Specification<Expense> expenseSpecification = expenseSpecificationBuilder.build(filterDto);

        Page<Expense> expensePage = expenseRepository
                .findAllByUserIdPaged(userId, expenseSpecification, pageable);

        return new GetTransactionsPageDto(expensePage.getNumber(),
                expensePage.getSize(),
                expensePage.getNumberOfElements(),
                expensePage.getTotalElements(),
                expensePage.getTotalPages(),
                transactionMapper.toExpenseDtoList(expensePage.getContent()));
    }

    @Override
    public List<SumsByPeriodDto> getSumOfTransactionsForPeriodOfTime(Long userId,
                                                      FilterTransactionByDaysDto transactionsDto) {
        if (transactionsDto.accountId() == null) {
            throw new IllegalArgumentException("Account id can't be null so as to prevent mixing "
                    + "transactions with different currencies");
        }
        presenceCheck(userId, transactionsDto);
        Specification<Expense> specification = expenseSpecificationBuilder.build(transactionsDto);
        Map<LocalDate, Map<String, BigDecimal>> categorizedExpenseSums =
                expenseRepository.findAllByUserIdUnpaged(userId, specification)
                        .stream()
                        .collect(Collectors.groupingBy(Expense::getTransactionDate,
                                getCollectorGroupByDateAndThenCategorySum()
                        ));
        return transactionsCommonFunctionsUtil.prepareListOfAccumulatedDtos(categorizedExpenseSums);
    }

    @Override
    public List<SumsByPeriodDto> getSumOfTransactionsForMonthOrYear(
            Long userId,
            FilterTransactionByMonthsYearsDto chartTransactionRequestDtoByMonthOrYear) {
        if (chartTransactionRequestDtoByMonthOrYear.accountId() == null) {
            throw new IllegalArgumentException("Account id can't be null so as to prevent mixing "
                    + "transactions with different currencies");
        }
        FilterTransactionByDaysDto filterTransactionsDto = transactionsCommonFunctionsUtil
                .getFilterDtoWithNoDates(chartTransactionRequestDtoByMonthOrYear);
        presenceCheck(userId, filterTransactionsDto);
        Specification<Expense> specification =
                expenseSpecificationBuilder.build(filterTransactionsDto);
        Map<LocalDate, Map<String, BigDecimal>> categorizedExpenseSums =
                expenseRepository.findAllByUserIdUnpaged(userId, specification)
                        .stream()
                        .collect(Collectors.groupingBy(
                                expense -> transactionsCommonFunctionsUtil
                                        .getPeriodDate(expense.getTransactionDate(),
                                                chartTransactionRequestDtoByMonthOrYear),
                                getCollectorGroupByDateAndThenCategorySum()
                        ));
        return transactionsCommonFunctionsUtil.prepareListOfAccumulatedDtos(categorizedExpenseSums);
    }

    @Override
    public SaveAndUpdateResponseDto updateTransaction(
                                               Long userId,
                                               UpdateTransactionDto requestTransactionDto,
                                               Long transactionId) {
        Expense thisExpense = expenseRepository
                .findByIdAndUserId(transactionId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No expense with id " + transactionId
                                        + " was found for user with id " + userId));
        if (thisExpense.getExpenseCategory().getName().equals(TARGET_EXPENSE_CATEGORY)) {
            throw new ConflictException("Can't modify expenses with "
                    + TARGET_EXPENSE_CATEGORY + " category");
        }

        ExpenseCategory newExpenseCategory;
        if (requestTransactionDto.getCategoryId() != null) {
            newExpenseCategory = checkIfCategoryValid(requestTransactionDto, userId);
            thisExpense.setExpenseCategory(newExpenseCategory);
        }

        Account newAccount = null;
        if (requestTransactionDto.getAccountId() != null) {
            newAccount = checkIfAccountValid(requestTransactionDto, userId);
        }

        //Change transactionAmount within the same account
        Account thisExpenseAccount = thisExpense.getAccount();
        if (newAccount == null && requestTransactionDto.getAmount() != null) {
            thisExpenseAccount.setBalance(thisExpenseAccount.getBalance()
                    .add(thisExpense.getAmount()));
            if (transactionsCommonFunctionsUtil
                    .isSufficientAmount(
                            thisExpenseAccount, requestTransactionDto.getAmount()) < 0) {
                throw new TransactionFailedException("Not enough money for transaction");
            }
            thisExpenseAccount.setBalance(thisExpenseAccount.getBalance()
                    .subtract(requestTransactionDto.getAmount()));
            accountRepository.save(thisExpenseAccount);
            thisExpense.setAmount(requestTransactionDto.getAmount());

        //Change account, but not the sum
        } else if (newAccount != null && requestTransactionDto.getAmount() == null) {
            thisExpenseAccount.setBalance(thisExpenseAccount.getBalance()
                    .add(thisExpense.getAmount()));
            if (transactionsCommonFunctionsUtil
                    .isSufficientAmount(newAccount, thisExpense.getAmount()) < 0) {
                throw new TransactionFailedException("Not enough money for transaction");
            }
            newAccount.setBalance(thisExpenseAccount.getBalance()
                    .subtract(thisExpense.getAmount()));
            accountRepository.save(thisExpenseAccount);
            accountRepository.save(newAccount);
            thisExpense.setAccount(newAccount);
            thisExpense.setCurrency(newAccount.getCurrency());

        //Change both
        } else if (newAccount != null) {
            thisExpenseAccount.setBalance(thisExpenseAccount.getBalance()
                    .add(thisExpense.getAmount()));
            if (transactionsCommonFunctionsUtil
                    .isSufficientAmount(newAccount, requestTransactionDto.getAmount()) < 0) {
                throw new TransactionFailedException("Not enough money for transaction");
            }
            newAccount.setBalance(thisExpenseAccount.getBalance()
                    .subtract(requestTransactionDto.getAmount()));
            accountRepository.save(thisExpenseAccount);
            accountRepository.save(newAccount);
            thisExpense.setAccount(newAccount);
            thisExpense.setAmount(requestTransactionDto.getAmount());
            thisExpense.setCurrency(newAccount.getCurrency());
        }
        if (requestTransactionDto.getTransactionDate() != null) {
            thisExpense.setTransactionDate(
                    LocalDate.parse(requestTransactionDto.getTransactionDate()));
        }
        if (requestTransactionDto.getComment() != null) {
            thisExpense.setComment(requestTransactionDto.getComment());
        }
        expenseRepository.save(thisExpense);
        return transactionMapper.toPersistExpenseDto(thisExpense);
    }

    @Override
    public void deleteByTransactionId(Long userId, Long transactionId) {
        Expense expense = expenseRepository
                .findByIdAndUserId(transactionId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No expense with id "
                                + transactionId + " was found for user with id " + userId));
        if (expense.getExpenseCategory().getName().equals(TARGET_EXPENSE_CATEGORY)) {
            throw new ConflictException("Can't modify expenses with "
                    + TARGET_EXPENSE_CATEGORY + " category");
        }
        Account account = accountRepository
                .findByIdAndUserId(expense.getAccount().getId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + expense.getAccount().getId()
                                + " was found for user with id " + userId));
        account.setBalance(account.getBalance().add(expense.getAmount()));
        accountRepository.save(account);
        expenseRepository.deleteById(transactionId);
    }

    private Collector<Expense, Object, Map<String, BigDecimal>>
            getCollectorGroupByDateAndThenCategorySum() {
        return Collectors.collectingAndThen(
                Collectors.groupingBy(Expense::getExpenseCategory,
                        Collectors.reducing(BigDecimal.ZERO,
                                Expense::getAmount, BigDecimal::add)),
                categoryMap -> {
                    BigDecimal dailyTotal = categoryMap.values()
                            .stream()
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    Map<String, BigDecimal> resultMap =
                            new LinkedHashMap<>();
                    resultMap.put("Sum", dailyTotal);
                    categoryMap.forEach((category, amount) ->
                            resultMap.put(category.getName(), amount));
                    return resultMap;
                }
        );
    }

    private void isCategoryPresentInDb(Long userId, Long categoryId) {
        if (!expenseCategoryRepository.existsByIdAndUserId(categoryId, userId)) {
            throw new EntityNotFoundException("No category with id " + categoryId
                    + " was found for user with id " + userId);
        }
    }

    private void presenceCheck(Long userId, FilterTransactionByDaysDto filterTransactionsDto) {
        if (filterTransactionsDto.accountId() != null) {
            if (!accountRepository.existsByIdAndUserId(filterTransactionsDto.accountId(), userId)) {
                throw new EntityNotFoundException("No account with id "
                        + filterTransactionsDto.accountId() + " for user with id "
                        + userId + " was found");
            }
        }
        if (filterTransactionsDto.categoryIds() != null) {
            for (Long categoryId : filterTransactionsDto.categoryIds()) {
                isCategoryPresentInDb(userId, categoryId);
            }
        }
    }

    private ExpenseCategory checkIfCategoryValid(UpdateTransactionDto requestTransactionDto,
                                                 Long userId) {
        ExpenseCategory newExpenseCategory = expenseCategoryRepository.findByIdAndUserId(
                        requestTransactionDto.getCategoryId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("No category with id "
                        + requestTransactionDto.getCategoryId()
                        + " was found for user with id " + userId));

        if (newExpenseCategory.getName().equals(TARGET_EXPENSE_CATEGORY)) {
            throw new ConflictException("Can't assign " + TARGET_EXPENSE_CATEGORY
                    + " inside an update");
        }
        return newExpenseCategory;
    }

    private Account checkIfAccountValid(UpdateTransactionDto requestTransactionDto, Long userId) {
        return accountRepository
                .findByIdAndUserId(requestTransactionDto.getAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.getAccountId()
                                + " was found for user with id " + userId));
    }
}
