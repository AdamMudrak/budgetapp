package com.example.budgetingapp.services.impl.transactions.expenses;

import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.EXPENSE;

import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transactions.expenses.Expense;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transactions.expenses.ExpenseRepository;
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
    public ResponseTransactionDto saveTransaction(RequestTransactionDto requestTransactionDto) {
        Expense expense = transactionMapper.toExpense(requestTransactionDto);
        Account account = accountRepository
                .findById(requestTransactionDto.accountId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.accountId() + " was found"));
        account.setBalance(account.getBalance().subtract(requestTransactionDto.amount()));
        accountRepository.save(account);
        expenseRepository.save(expense);
        return transactionMapper.toExpenseDto(expense);
    }

    @Override
    public List<ResponseTransactionDto> getAllTransactions(Pageable pageable) {
        return expenseRepository
                .findAll(pageable)
                .stream()
                .map(transactionMapper::toExpenseDto)
                .toList();
    }

    @Transactional
    @Override
    public ResponseTransactionDto updateTransaction(RequestTransactionDto requestTransactionDto,
                                                    Long transactionId) {
        Expense previousExpense = expenseRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No expense with id " + transactionId + " was found"));
        Account account = accountRepository
                .findById(requestTransactionDto.accountId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.accountId() + " was found"));
        account.setBalance(account.getBalance().add(previousExpense.getAmount()));

        Expense newExpense = transactionMapper.toExpense(requestTransactionDto);
        newExpense.setId(transactionId);
        account.setBalance(account.getBalance().subtract(requestTransactionDto.amount()));
        accountRepository.save(account);
        expenseRepository.save(newExpense);
        return transactionMapper.toExpenseDto(newExpense);
    }

    @Transactional
    @Override
    public void deleteByTransactionId(Long transactionId, Long accountId) {
        Expense expense = expenseRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No expense with id "
                                + transactionId + " was found"));
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + accountId + " was found"));
        account.setBalance(account.getBalance().add(expense.getAmount()));
        accountRepository.save(account);
        expenseRepository.deleteById(transactionId);
    }
}
