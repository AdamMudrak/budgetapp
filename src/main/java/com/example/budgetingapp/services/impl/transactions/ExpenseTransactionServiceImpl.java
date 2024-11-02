package com.example.budgetingapp.services.impl.transactions;

import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.EXPENSE;

import com.example.budgetingapp.dtos.transactions.request.ChartTransactionRequestDto;
import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.AccumulatedResultDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.TransactionSumByCategoryDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.exceptions.conflictexpections.TransactionFailedException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.CategoryMapper;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.ExpenseSpecificationBuilder;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.TransactionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    private static final int FIRST_DAY = 1;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final CategoryMapper categoryMapper;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseSpecificationBuilder expenseSpecificationBuilder;

    @Transactional
    @Override
    public ResponseTransactionDto saveTransaction(Long userId,
                                                  RequestTransactionDto requestTransactionDto) {

        Account account = accountRepository
                .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id "
                                + requestTransactionDto.accountId() + " was found"
                                + " for user with id " + userId));
        if (isSufficientAmount(account, requestTransactionDto) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        account.setBalance(account.getBalance().subtract(requestTransactionDto.amount()));
        accountRepository.save(account);
        Expense expense = transactionMapper.toExpense(requestTransactionDto);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        expense.setUser(currentUser);
        expenseRepository.save(expense);
        return transactionMapper.toExpenseDto(expense);
    }

    @Override
    public List<ResponseTransactionDto> getAllTransactions(Long userId,
                                                           FilterTransactionsDto filterDto,
                                                           Pageable pageable) {
        Specification<Expense> expenseSpecification = expenseSpecificationBuilder.build(filterDto);
        return expenseRepository.findAll(expenseSpecification, pageable)
                .stream()
                .map(transactionMapper::toExpenseDto)
                .toList();
    }

    //TODO REFACTOR, IMPROVE
    @Override
    public List<AccumulatedResultDto> getSumOfTransactionsForPeriodOfTime(Long userId,
                                          ChartTransactionRequestDto chartTransactionRequestDto) {
        Map<LocalDate, Map<ExpenseCategory, BigDecimal>> categorizedExpenseSums =
                expenseRepository.findAll()
                .stream()
                .filter(expense -> isDateWithinPeriod(expense.getTransactionDate(),
                        chartTransactionRequestDto))
                .collect(Collectors.groupingBy(
                        expense -> getPeriodDate(expense.getTransactionDate(),
                                chartTransactionRequestDto),
                        Collectors.groupingBy(Expense::getExpenseCategory,
                                Collectors.reducing(BigDecimal.ZERO,
                                        Expense::getAmount, BigDecimal::add)
                        )
                ));
        return categorizedExpenseSums.entrySet().stream()
                .map(entry -> {
                    List<TransactionSumByCategoryDto> sumsByDate = entry
                            .getValue()
                            .entrySet()
                            .stream()
                            .map(dateEntry -> new TransactionSumByCategoryDto(
                                    categoryMapper.toExpenseCategoryDto(dateEntry.getKey()),
                                    dateEntry.getValue()))
                            .collect(Collectors.toList());
                    return new AccumulatedResultDto(entry.getKey(), sumsByDate);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ResponseTransactionDto updateTransaction(Long userId,
                                                    RequestTransactionDto requestTransactionDto,
                                                    Long transactionId) {
        Expense previousExpense = expenseRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No expense with id " + transactionId + " was found"));

        Account previousAccount = accountRepository
                .findByIdAndUserId(previousExpense.getAccount().getId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + previousExpense.getAccount().getId()
                                + " was found for user with id " + userId));
        previousAccount.setBalance(previousAccount.getBalance().add(previousExpense.getAmount()));
        accountRepository.save(previousAccount);

        Account newAccount = accountRepository
                .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.accountId()
                                + " was found for user with id " + userId));
        if (isSufficientAmount(newAccount, requestTransactionDto) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        newAccount.setBalance(newAccount.getBalance().subtract(requestTransactionDto.amount()));
        accountRepository.save(newAccount);

        Expense newExpense = transactionMapper.toExpense(requestTransactionDto);
        newExpense.setId(transactionId);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        newExpense.setUser(currentUser);
        expenseRepository.save(newExpense);
        return transactionMapper.toExpenseDto(newExpense);
    }

    @Transactional
    @Override
    public void deleteByTransactionId(Long userId, Long transactionId) {
        Expense expense = expenseRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No expense with id "
                                + transactionId + " was found"));
        Account account = accountRepository
                .findByIdAndUserId(expense.getAccount().getId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + expense.getAccount().getId()
                                + " was found for user with id " + userId));
        account.setBalance(account.getBalance().add(expense.getAmount()));
        accountRepository.save(account);
        expenseRepository.deleteById(transactionId);
    }

    private int isSufficientAmount(Account account, RequestTransactionDto requestTransactionDto) {
        return (account.getBalance()
                .subtract(requestTransactionDto.amount()))
                .compareTo(BigDecimal.ZERO);
    }

    private boolean isDateWithinPeriod(LocalDate checkDate,
                               ChartTransactionRequestDto accumulatedTransactionRequestDto) {
        if (accumulatedTransactionRequestDto.getFromDate() == null
                && accumulatedTransactionRequestDto.getToDate() == null) {
            return true;
        }
        return (checkDate.isAfter(accumulatedTransactionRequestDto.getFromDate())
                || checkDate.isEqual(accumulatedTransactionRequestDto.getFromDate())
                && checkDate.isBefore(accumulatedTransactionRequestDto.getToDate())
                || checkDate.isEqual(accumulatedTransactionRequestDto.getToDate()));
    }

    private LocalDate getPeriodDate(LocalDate transactionDate,
                                    ChartTransactionRequestDto chartTransactionRequestDto) {
        return switch (chartTransactionRequestDto.getFilterType()) {
            case DAY -> transactionDate;
            case MONTH -> transactionDate.withDayOfMonth(FIRST_DAY);
            case YEAR -> transactionDate.withDayOfYear(FIRST_DAY);
        };
    }
}
