package com.example.budgetingapp.services.impl.transactions.incomes;

import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.INCOME;

import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transactions.incomes.Income;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transactions.incomes.IncomeRepository;
import com.example.budgetingapp.services.TransactionService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Qualifier(INCOME)
@RequiredArgsConstructor
public class IncomeTransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final IncomeRepository incomeRepository;

    @Transactional
    @Override
    public ResponseTransactionDto saveTransaction(RequestTransactionDto requestTransactionDto) {
        Income income = transactionMapper.toIncome(requestTransactionDto);
        Account account = accountRepository
                .findById(requestTransactionDto.accountId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.accountId() + " was found"));
        account.setBalance(account.getBalance().add(requestTransactionDto.amount()));
        accountRepository.save(account);
        incomeRepository.save(income);
        return transactionMapper.toIncomeDto(income);
    }

    @Override
    public List<ResponseTransactionDto> getAllTransactions(Pageable pageable) {
        return incomeRepository
                .findAll(pageable)
                .stream()
                .map(transactionMapper::toIncomeDto)
                .toList();
    }

    @Transactional
    @Override
    public ResponseTransactionDto updateTransaction(RequestTransactionDto requestTransactionDto,
                                                    Long transactionId) {
        Income previousIncome = incomeRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No income with id " + transactionId + " was found"));
        Account account = accountRepository
                .findById(requestTransactionDto.accountId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.accountId() + " was found"));
        account.setBalance(account.getBalance().subtract(previousIncome.getAmount()));

        Income newIncome = transactionMapper.toIncome(requestTransactionDto);
        newIncome.setId(transactionId);
        account.setBalance(account.getBalance().add(requestTransactionDto.amount()));
        accountRepository.save(account);
        incomeRepository.save(previousIncome);
        return transactionMapper.toIncomeDto(newIncome);
    }

    @Transactional
    @Override
    public void deleteByTransactionId(Long transactionId, Long accountId) {
        Income income = incomeRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No income with id "
                                + transactionId + " was found"));
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + accountId + " was found"));
        account.setBalance(account.getBalance().subtract(income.getAmount()));
        accountRepository.save(account);
        incomeRepository.deleteById(transactionId);
    }
}
