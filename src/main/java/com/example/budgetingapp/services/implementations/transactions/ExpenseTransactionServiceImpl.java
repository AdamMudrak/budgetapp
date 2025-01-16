package com.example.budgetingapp.services.implementations.transactions;

import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.EXPENSE;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateRequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByMonthOrYear;
import com.example.budgetingapp.dtos.transactions.response.AccumulatedResultDto;
import com.example.budgetingapp.dtos.transactions.response.GetResponseTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseTransactionDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.exceptions.conflictexpections.TransactionFailedException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.categories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.ExpenseSpecificationBuilder;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.interfaces.TransactionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Qualifier(EXPENSE)
@RequiredArgsConstructor
public class ExpenseTransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseSpecificationBuilder expenseSpecificationBuilder;
    private final TransactionsCommonFunctionsUtil transactionsCommonFunctionsUtil;

    @Transactional
    @Override
    public SaveAndUpdateResponseTransactionDto saveTransaction(Long userId,
                                                   RequestTransactionDto requestTransactionDto) {
        isCategoryPresentInDb(userId, requestTransactionDto.categoryId());
        Account account = accountRepository
                .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id "
                                + requestTransactionDto.accountId() + " was found"
                                + " for user with id " + userId));
        if (transactionsCommonFunctionsUtil
                .isSufficientAmount(account, requestTransactionDto) < 0) {
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
    public List<GetResponseTransactionDto> getAllTransactions(Long userId,
                                                              FilterTransactionsDto filterDto,
                                                              Pageable pageable) {
        presenceCheck(userId, filterDto);
        Specification<Expense> expenseSpecification = expenseSpecificationBuilder.build(filterDto);
        return expenseRepository.findAllByUserIdPaged(userId, expenseSpecification, pageable)
                .stream()
                .map(transactionMapper::toExpenseDto)
                .sorted(Comparator.comparing(GetResponseTransactionDto::transactionDate).reversed())
                .toList();
    }

    @Override
    public List<AccumulatedResultDto> getSumOfTransactionsForPeriodOfTime(Long userId,
                                                          FilterTransactionsDto transactionsDto) {
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
    public List<AccumulatedResultDto> getSumOfTransactionsForMonthOrYear(
            Long userId,
            ChartTransactionRequestDtoByMonthOrYear chartTransactionRequestDtoByMonthOrYear) {
        if (chartTransactionRequestDtoByMonthOrYear.accountId() == null) {
            throw new IllegalArgumentException("Account id can't be null so as to prevent mixing "
                    + "transactions with different currencies");
        }
        FilterTransactionsDto filterTransactionsDto = transactionsCommonFunctionsUtil
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

    @Transactional
    @Override
    public SaveAndUpdateResponseTransactionDto updateTransaction(
                                               Long userId,
                                               UpdateRequestTransactionDto requestTransactionDto,
                                               Long transactionId) {
        String currency = "";
        presenceCheck(userId, requestTransactionDto);
        Expense previousExpense = expenseRepository
                .findByIdAndUserId(transactionId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No expense with id " + transactionId
                                        + " was found for user with id " + userId));
        if (requestTransactionDto.amount() != null) {
            Account thisExpenseAccount = accountRepository
                    .findByIdAndUserId(previousExpense.getAccount().getId(), userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "No account with id " + previousExpense.getAccount().getId()
                                    + " was found for user with id " + userId));
            thisExpenseAccount.setBalance(
                    thisExpenseAccount.getBalance().add(previousExpense.getAmount()));
            accountRepository.save(thisExpenseAccount);

            Account currentAccount;
            if (requestTransactionDto.accountId() != null) {
                currentAccount = accountRepository
                        .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No account with id " + requestTransactionDto.accountId()
                                        + " was found for user with id " + userId));
                currency = currentAccount.getCurrency();
            } else {
                currentAccount = thisExpenseAccount;
            }

            if (transactionsCommonFunctionsUtil
                    .isSufficientAmount(currentAccount, requestTransactionDto) < 0) {
                throw new TransactionFailedException("Not enough money for transaction");
            } else {
                currentAccount.setBalance(currentAccount.getBalance()
                        .subtract(requestTransactionDto.amount()));
                accountRepository.save(currentAccount);
            }
        }

        Expense newExpense = transactionMapper.toExpense(requestTransactionDto);
        newExpense.setId(transactionId);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        newExpense.setUser(currentUser);
        if (requestTransactionDto.accountId() != null) {
            newExpense.setCurrency(currency);
        }
        expenseRepository.save(newExpense);
        return transactionMapper.toPersistExpenseDto(newExpense);
    }

    @Transactional
    @Override
    public void deleteByTransactionId(Long userId, Long transactionId) {
        Expense expense = expenseRepository
                .findByIdAndUserId(transactionId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No expense with id "
                                + transactionId + " was found for user with id " + userId));
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
                    resultMap.put("Sum for date:", dailyTotal);
                    categoryMap.forEach((category, amount) ->
                            resultMap.put("Sum for category "
                                    + category.getName() + ":", amount));
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

    private void presenceCheck(Long userId, FilterTransactionsDto filterTransactionsDto) {
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

    private void presenceCheck(Long userId, UpdateRequestTransactionDto requestTransactionDto) {
        if (!accountRepository.existsByIdAndUserId(requestTransactionDto.accountId(), userId)) {
            throw new EntityNotFoundException("No account with id "
                    + requestTransactionDto.accountId() + " for user with id "
                    + userId + " was found");
        }
        isCategoryPresentInDb(userId, requestTransactionDto.categoryId());
    }
}
