package com.example.budgetingapp.services.impl.transactions;

import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.EXPENSE;

import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.services.TransactionService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Qualifier(EXPENSE)
@RequiredArgsConstructor
public class ExpenseTransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final ExpenseRepository expenseRepository;

    @Transactional
    @Override
    public ResponseTransactionDto saveTransaction(Long userId,
                                                  RequestTransactionDto requestTransactionDto) {
        Expense expense = transactionMapper.toExpense(requestTransactionDto);
        Account account = accountRepository
                .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.accountId() + " was found"));
        account.setBalance(account.getBalance().add(requestTransactionDto.amount()));
        accountRepository.save(account);
        expenseRepository.save(expense);
        return transactionMapper.toExpenseDto(expense);
    }

    @Override
    public List<ResponseTransactionDto> getAllTransactions(Long userId,
                                                           Pageable pageable) {
        return expenseRepository
                .findAllByUserId(userId, pageable)
                .stream()
                .map(transactionMapper::toExpenseDto)
                .toList();
    }

    @Override
    public List<ResponseTransactionDto> getAllAccountTransactions(Long userId,
                                                                  Long accountId,
                                                                  Pageable pageable) {
        if (accountRepository.existsByIdAndUserId(accountId, userId)) {
            throw new EntityNotFoundException("No account with id " + accountId
                    + " for user with id " + userId + " was found");
        }
        return expenseRepository
                .findAllByAccountId(accountId, pageable)
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
        Account account = accountRepository
                .findByIdAndUserId(previousExpense.getAccount().getId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + previousExpense.getAccount().getId()
                                + " was found for user with id " + userId));

        Expense newExpense = transactionMapper.toExpense(requestTransactionDto);
        newExpense.setId(transactionId);
        account.setBalance(account.getBalance().add(requestTransactionDto.amount()));
        accountRepository.save(account);
        expenseRepository.save(previousExpense);
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
        account.setBalance(account.getBalance().subtract(expense.getAmount()));
        accountRepository.save(account);
        expenseRepository.deleteById(transactionId);
    }
}
