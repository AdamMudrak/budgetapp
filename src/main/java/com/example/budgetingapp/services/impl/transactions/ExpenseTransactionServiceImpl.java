package com.example.budgetingapp.services.impl.transactions;

import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.EXPENSE;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.TransactionFailedException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.ExpenseSpecificationBuilder;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.TransactionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
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
    private final ExpenseSpecificationBuilder expenseSpecificationBuilder;

    @Transactional
    @Override
    public ResponseTransactionDto saveTransaction(Long userId,
                                                  RequestTransactionDto requestTransactionDto) {

        Account account = accountRepository
                .findByIdAndUserId(requestTransactionDto.getAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id "
                                + requestTransactionDto.getAccountId() + " was found"
                                + " for user with id " + userId));
        if (isSufficientAmount(account, requestTransactionDto) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        account.setBalance(account.getBalance().subtract(requestTransactionDto.getAmount()));
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
                .findByIdAndUserId(requestTransactionDto.getAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.getAccountId()
                                + " was found for user with id " + userId));
        if (isSufficientAmount(newAccount, requestTransactionDto) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        newAccount.setBalance(newAccount.getBalance().subtract(requestTransactionDto.getAmount()));
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
                .subtract(requestTransactionDto.getAmount()))
                .compareTo(BigDecimal.ZERO);
    }
}
