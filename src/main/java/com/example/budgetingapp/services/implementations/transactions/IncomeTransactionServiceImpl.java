package com.example.budgetingapp.services.implementations.transactions;

import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.INCOME;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateRequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByMonthOrYear;
import com.example.budgetingapp.dtos.transactions.response.ChartsAccumulatedResultDto;
import com.example.budgetingapp.dtos.transactions.response.GetResponseTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.GetTransactionsPageDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseTransactionDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.exceptions.conflictexpections.TransactionFailedException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.categories.IncomeCategoryRepository;
import com.example.budgetingapp.repositories.transactions.IncomeRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.income.IncomeSpecificationBuilder;
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
import org.springframework.data.domain.Page;
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
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final IncomeSpecificationBuilder incomeSpecificationBuilder;
    private final TransactionsCommonFunctionsUtil transactionsCommonFunctionsUtil;

    @Transactional
    @Override
    public SaveAndUpdateResponseTransactionDto saveTransaction(Long userId,
                                                   RequestTransactionDto requestTransactionDto) {
        isCategoryPresentInDb(userId, requestTransactionDto.categoryId());
        Income income = transactionMapper.toIncome(requestTransactionDto);
        Account account = accountRepository
                .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id "
                                + requestTransactionDto.accountId()
                                + " was found for user with id " + userId));
        account.setBalance(account.getBalance().add(requestTransactionDto.amount()));
        accountRepository.save(account);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        income.setUser(currentUser);
        income.setCurrency(account.getCurrency());
        incomeRepository.save(income);
        return transactionMapper.toPersistIncomeDto(income);
    }

    @Override
    public GetTransactionsPageDto getAllTransactions(Long userId,
                                                     FilterTransactionsDto filterDto,
                                                     Pageable pageable) {
        presenceCheck(userId, filterDto);
        Specification<Income> incomeSpecification = incomeSpecificationBuilder.build(filterDto);

        Page<Income> incomePage =
                incomeRepository.findAllByUserIdPaged(userId, incomeSpecification, pageable);

        List<GetResponseTransactionDto> transactionDtos = incomePage.stream()
                .map(transactionMapper::toIncomeDto)
                .sorted(Comparator.comparing(GetResponseTransactionDto::transactionDate).reversed())
                .toList();

        return new GetTransactionsPageDto(incomePage.getNumber(),
                incomePage.getSize(),
                incomePage.getNumberOfElements(),
                incomePage.getTotalElements(),
                incomePage.getTotalPages(),
                transactionDtos);
    }

    @Override
    public List<ChartsAccumulatedResultDto> getSumOfTransactionsForPeriodOfTime(Long userId,
                                                            FilterTransactionsDto transactionsDto) {
        if (transactionsDto.accountId() == null) {
            throw new IllegalArgumentException("Account id can't be null so as to prevent mixing "
                    + "transactions with different currencies");
        }
        presenceCheck(userId, transactionsDto);
        Specification<Income> specification = incomeSpecificationBuilder.build(transactionsDto);
        Map<LocalDate, Map<String, BigDecimal>> categorizedIncomeSums =
                incomeRepository.findAllByUserIdUnpaged(userId, specification)
                        .stream()
                        .collect(Collectors.groupingBy(Income::getTransactionDate,
                                getCollectorGroupByDateAndThenCategorySum()
                        ));
        return transactionsCommonFunctionsUtil.prepareListOfAccumulatedDtos(categorizedIncomeSums);
    }

    @Override
    public List<ChartsAccumulatedResultDto> getSumOfTransactionsForMonthOrYear(Long userId,
               ChartTransactionRequestDtoByMonthOrYear chartTransactionRequestDtoByMonthOrYear) {
        if (chartTransactionRequestDtoByMonthOrYear.accountId() == null) {
            throw new IllegalArgumentException("Account id can't be null so as to prevent mixing "
                    + "transactions with different currencies");
        }
        FilterTransactionsDto filterTransactionsDto = transactionsCommonFunctionsUtil
                .getFilterDtoWithNoDates(chartTransactionRequestDtoByMonthOrYear);
        presenceCheck(userId, filterTransactionsDto);
        Specification<Income> specification =
                incomeSpecificationBuilder.build(filterTransactionsDto);
        Map<LocalDate, Map<String, BigDecimal>> categorizedIncomeSums =
                incomeRepository.findAllByUserIdUnpaged(userId, specification)
                        .stream()
                        .collect(Collectors.groupingBy(
                                income -> transactionsCommonFunctionsUtil
                                        .getPeriodDate(income.getTransactionDate(),
                                                chartTransactionRequestDtoByMonthOrYear),
                                getCollectorGroupByDateAndThenCategorySum()
                        ));
        return transactionsCommonFunctionsUtil.prepareListOfAccumulatedDtos(categorizedIncomeSums);
    }

    @Transactional
    @Override
    public SaveAndUpdateResponseTransactionDto updateTransaction(
                                               Long userId,
                                               UpdateRequestTransactionDto requestTransactionDto,
                                               Long transactionId) {
        String currency = "";
        presenceCheck(userId, requestTransactionDto);
        Income previousIncome = incomeRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No income with id " + transactionId + " was found"));
        if (requestTransactionDto.amount() != null) {
            Account thisIncomeAccount = accountRepository
                    .findByIdAndUserId(previousIncome.getAccount().getId(), userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "No account with id " + previousIncome.getAccount().getId()
                                    + " was found for user with id " + userId));

            if (transactionsCommonFunctionsUtil
                    .isSufficientAmount(thisIncomeAccount, previousIncome) < 0) {
                throw new TransactionFailedException("Not enough money for transaction");
            } else {
                thisIncomeAccount.setBalance(thisIncomeAccount.getBalance()
                        .subtract(previousIncome.getAmount()));
                accountRepository.save(thisIncomeAccount);
            }

            Account currentAccount;
            if (requestTransactionDto.accountId() != null) {
                currentAccount = accountRepository
                        .findByIdAndUserId(requestTransactionDto.accountId(), userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No account with id " + requestTransactionDto.accountId()
                                        + " was found for user with id " + userId));
                currency = currentAccount.getCurrency();
            } else {
                currentAccount = thisIncomeAccount;
            }
            currentAccount.setBalance(
                    currentAccount.getBalance().add(requestTransactionDto.amount()));
            accountRepository.save(currentAccount);
        }

        Income newIncome = transactionMapper.toIncome(requestTransactionDto);
        newIncome.setId(transactionId);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        newIncome.setUser(currentUser);
        if (requestTransactionDto.accountId() != null) {
            newIncome.setCurrency(currency);
        }
        incomeRepository.save(newIncome);
        return transactionMapper.toPersistIncomeDto(newIncome);
    }

    @Transactional
    @Override
    public void deleteByTransactionId(Long userId, Long transactionId) {
        Income income = incomeRepository
                .findByIdAndUserId(transactionId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No income with id "
                                + transactionId + " was found for user with id " + userId));
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

    private void isCategoryPresentInDb(Long userId, Long categoryId) {
        if (!incomeCategoryRepository.existsByIdAndUserId(categoryId, userId)) {
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
