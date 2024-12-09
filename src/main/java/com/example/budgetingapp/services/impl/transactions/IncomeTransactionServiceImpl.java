package com.example.budgetingapp.services.impl.transactions;

import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.INCOME;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByDay;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByMonthOrYear;
import com.example.budgetingapp.dtos.transactions.response.AccumulatedResultDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.helper.TransactionSumByCategoryDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.exceptions.conflictexpections.TransactionFailedException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transactions.IncomeRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.income.IncomeSpecificationBuilder;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.TransactionService;
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
@Qualifier(INCOME)
@RequiredArgsConstructor
public class IncomeTransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;
    private final IncomeRepository incomeRepository;
    private final IncomeSpecificationBuilder incomeSpecificationBuilder;
    private final TransactionsCommonFunctionsUtil transactionsCommonFunctionsUtil;

    @Transactional
    @Override
    public ResponseTransactionDto saveTransaction(Long userId,
                                                  RequestTransactionDto requestTransactionDto) {
        Income income = transactionMapper.toIncome(requestTransactionDto);
        Account account = accountRepository
                .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id "
                                + requestTransactionDto.accountId() + " was found"));
        account.setBalance(account.getBalance().add(requestTransactionDto.amount()));
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
                .sorted(Comparator.comparing(Income::getTransactionDate))
                .map(transactionMapper::toIncomeDto)
                .toList();
    }

    @Override
    public List<AccumulatedResultDto> getSumOfTransactionsForPeriodOfTime(Long userId,
                              ChartTransactionRequestDtoByDay chartTransactionRequestDtoByDay) {
        Map<LocalDate, Map<String, BigDecimal>> categorizedExpenseSums =
                incomeRepository.findAll()
                        .stream()
                        .filter(income -> transactionsCommonFunctionsUtil
                                .isDateWithinPeriod(income.getTransactionDate(),
                                        chartTransactionRequestDtoByDay))
                        .collect(Collectors.groupingBy(Income::getTransactionDate,
                                getCollectorGroupByDateAndThenCategorySum()
                        ));
        return prepareListOfAccumulatedDtos(categorizedExpenseSums);
    }

    @Override
    public List<AccumulatedResultDto> getSumOfTransactionsForMonthOrYear(Long userId,
                ChartTransactionRequestDtoByMonthOrYear chartTransactionRequestDtoByMonthOrYear) {
        Map<LocalDate, Map<String, BigDecimal>> categorizedExpenseSums =
                incomeRepository.findAll()
                        .stream()
                        .collect(Collectors.groupingBy(
                                income -> transactionsCommonFunctionsUtil
                                        .getPeriodDate(income.getTransactionDate(),
                                                chartTransactionRequestDtoByMonthOrYear),
                                getCollectorGroupByDateAndThenCategorySum()
                        ));
        return prepareListOfAccumulatedDtos(categorizedExpenseSums);
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

        Account previousAccount = accountRepository
                .findByIdAndUserId(previousIncome.getAccount().getId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + previousIncome.getAccount().getId()
                                + " was found for user with id " + userId));

        if (transactionsCommonFunctionsUtil
                .isSufficientAmount(previousAccount, previousIncome) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        previousAccount.setBalance(previousAccount.getBalance()
                .subtract(previousIncome.getAmount()));
        accountRepository.save(previousAccount);

        Account newAccount = accountRepository
                .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.accountId()
                                + " was found for user with id " + userId));
        newAccount.setBalance(newAccount.getBalance().add(requestTransactionDto.amount()));
        accountRepository.save(newAccount);

        Income newIncome = transactionMapper.toIncome(requestTransactionDto);
        newIncome.setId(transactionId);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        newIncome.setUser(currentUser);
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
        if (transactionsCommonFunctionsUtil.isSufficientAmount(account, income) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        account.setBalance(account.getBalance().subtract(income.getAmount()));
        accountRepository.save(account);
        incomeRepository.deleteById(transactionId);
    }

    private List<AccumulatedResultDto> prepareListOfAccumulatedDtos(
            Map<LocalDate, Map<String, BigDecimal>> categorizedExpenseSums) {
        return categorizedExpenseSums.entrySet().stream()
                .map(entry -> {
                    List<TransactionSumByCategoryDto> sumsByDate = entry
                            .getValue()
                            .entrySet()
                            .stream()
                            .map(dateEntry -> new TransactionSumByCategoryDto(
                                    dateEntry.getKey(),
                                    dateEntry.getValue()))
                            .collect(Collectors.toList());
                    return new AccumulatedResultDto(entry.getKey(), sumsByDate);
                })
                .sorted(Comparator.comparing(AccumulatedResultDto::localDate))
                .collect(Collectors.toList());
    }

    private Collector<Income, Object, Map<String, BigDecimal>>
            getCollectorGroupByDateAndThenCategorySum() {
        return Collectors.collectingAndThen(
                Collectors.groupingBy(Income::getIncomeCategory,
                        Collectors.reducing(BigDecimal.ZERO,
                                Income::getAmount, BigDecimal::add)),
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
}
