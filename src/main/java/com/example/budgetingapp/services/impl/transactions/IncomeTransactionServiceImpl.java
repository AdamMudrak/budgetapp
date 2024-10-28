package com.example.budgetingapp.services.impl.transactions;

import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.INCOME;

import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transactions.IncomeRepository;
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
    public ResponseTransactionDto saveTransaction(Long userId,
                                                  RequestTransactionDto requestTransactionDto) {
        Income income = transactionMapper.toIncome(requestTransactionDto);
        Account account = accountRepository
                .findByIdAndUserId(requestTransactionDto.getAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id "
                                + requestTransactionDto.getAccountId() + " was found"));
        account.setBalance(account.getBalance().add(requestTransactionDto.getAmount()));
        accountRepository.save(account);
        incomeRepository.save(income);
        return transactionMapper.toIncomeDto(income);
    }

    @Override
    public List<ResponseTransactionDto> getAllTransactions(Long userId,
                                                                  Pageable pageable) {
        return incomeRepository
                .findAllByUserId(userId, pageable)
                .stream()
                .map(transactionMapper::toIncomeDto)
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
        return incomeRepository
                .findAllByAccountId(accountId, pageable)
                .stream()
                .map(transactionMapper::toIncomeDto)
                .toList();
    }

    @Transactional
    @Override
    public ResponseTransactionDto updateTransaction(Long userId,
                                                    RequestTransactionDto requestTransactionDto,
                                                    Long transactionId) {
        Income previousIncome = incomeRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No income with id " + transactionId + " was found"));
        Account account = accountRepository
                .findByIdAndUserId(previousIncome.getAccount().getId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + previousIncome.getAccount().getId()
                                + " was found for user with id " + userId));

        Income newIncome = transactionMapper.toIncome(requestTransactionDto);
        newIncome.setId(transactionId);
        account.setBalance(account.getBalance().add(requestTransactionDto.getAmount()));
        accountRepository.save(account);
        incomeRepository.save(previousIncome);
        return transactionMapper.toIncomeDto(newIncome);
    }

    @Transactional
    @Override
    public void deleteByTransactionId(Long userId, Long transactionId) {
        Income income = incomeRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No income with id "
                                + transactionId + " was found"));
        Account account = accountRepository
                .findByIdAndUserId(income.getAccount().getId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + income.getAccount().getId()
                                + " was found for user with id " + userId));
        account.setBalance(account.getBalance().subtract(income.getAmount()));
        accountRepository.save(account);
        incomeRepository.deleteById(transactionId);
    }
}
