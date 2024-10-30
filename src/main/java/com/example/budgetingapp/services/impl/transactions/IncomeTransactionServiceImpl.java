package com.example.budgetingapp.services.impl.transactions;

import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.INCOME;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.TransactionFailedException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transactions.IncomeRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.income.IncomeSpecificationBuilder;
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
@Qualifier(INCOME)
@RequiredArgsConstructor
public class IncomeTransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;
    private final IncomeRepository incomeRepository;
    private final IncomeSpecificationBuilder incomeSpecificationBuilder;

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
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        income.setUser(currentUser);
        incomeRepository.save(income);
        return transactionMapper.toIncomeDto(income);
    }

    @Override
    public List<ResponseTransactionDto> getAllTransactions(Long userId,
                                                           FilterTransactionsDto filterDto,
                                                           Pageable pageable) {
        Specification<Income> incomeSpecification = incomeSpecificationBuilder.build(filterDto);
        return incomeRepository.findAll(incomeSpecification, pageable)
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
        if (isSufficientAmount(account, previousIncome) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        account.setBalance(account.getBalance().subtract(previousIncome.getAmount()));
        account.setBalance(account.getBalance().add(requestTransactionDto.getAmount()));
        accountRepository.save(account);
        incomeRepository.save(newIncome);
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
        if (isSufficientAmount(account, income) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        account.setBalance(account.getBalance().subtract(income.getAmount()));
        accountRepository.save(account);
        incomeRepository.deleteById(transactionId);
    }

    private int isSufficientAmount(Account account, Income income) {
        return (account.getBalance()
                .subtract(income.getAmount()))
                .compareTo(BigDecimal.ZERO);
    }
}
