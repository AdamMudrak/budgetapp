package com.example.budgetingapp.services.impl.transactions;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.TARGET_INCOME_CATEGORY;

import com.example.budgetingapp.dtos.transactions.request.CreateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByMonthsYearsDto;
import com.example.budgetingapp.dtos.transactions.response.GetTransactionsPageDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseDto;
import com.example.budgetingapp.dtos.transactions.response.charts.SumsByPeriodDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.categories.IncomeCategory;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.exceptions.ConflictException;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.TransactionFailedException;
import com.example.budgetingapp.mappers.TransactionMapper;
import com.example.budgetingapp.repositories.AccountRepository;
import com.example.budgetingapp.repositories.IncomeCategoryRepository;
import com.example.budgetingapp.repositories.UserRepository;
import com.example.budgetingapp.repositories.transactions.IncomeRepository;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.income.IncomeSpecificationBuilder;
import com.example.budgetingapp.services.TransactionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@Qualifier("INCOME")
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
    public SaveAndUpdateResponseDto saveTransaction(Long userId,
                                                    CreateTransactionDto requestTransactionDto) {
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
                                                     FilterTransactionByDaysDto filterDto,
                                                     Pageable pageable) {
        presenceCheck(userId, filterDto);
        Specification<Income> incomeSpecification = incomeSpecificationBuilder.build(filterDto);

        Page<Income> incomePage =
                incomeRepository.findAllByUserIdPaged(userId, incomeSpecification, pageable);

        return new GetTransactionsPageDto(incomePage.getNumber(),
                incomePage.getSize(),
                incomePage.getNumberOfElements(),
                incomePage.getTotalElements(),
                incomePage.getTotalPages(),
                transactionMapper.toIncomeDtoList(incomePage.getContent()));
    }

    @Override
    public List<SumsByPeriodDto> getSumOfTransactionsForPeriodOfTime(Long userId,
                                                    FilterTransactionByDaysDto transactionsDto) {
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
    public List<SumsByPeriodDto> getSumOfTransactionsForMonthOrYear(Long userId,
                     FilterTransactionByMonthsYearsDto chartTransactionRequestDtoByMonthOrYear) {
        if (chartTransactionRequestDtoByMonthOrYear.accountId() == null) {
            throw new IllegalArgumentException("Account id can't be null so as to prevent mixing "
                    + "transactions with different currencies");
        }
        FilterTransactionByDaysDto filterTransactionsDto = transactionsCommonFunctionsUtil
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
    public SaveAndUpdateResponseDto updateTransaction(
                                               Long userId,
                                               UpdateTransactionDto requestTransactionDto,
                                               Long transactionId) {
        Income thisIncome = incomeRepository
                .findByIdAndUserId(transactionId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No income with id " + transactionId
                                        + " was found for user with id " + userId));

        if (thisIncome.getIncomeCategory().getName().equals(TARGET_INCOME_CATEGORY)) {
            throw new ConflictException("Can't modify incomes with "
                    + TARGET_INCOME_CATEGORY + " category");
        }

        if (requestTransactionDto.getCategoryId() != null) {
            thisIncome.setIncomeCategory(getNewCategoryIfValid(requestTransactionDto, userId));
        }

        Account newAccount = null;
        if (requestTransactionDto.getAccountId() != null) {
            newAccount = getNewAccountIfValid(requestTransactionDto, userId);
        }

        //Change transactionAmount within the same account
        Account thisIncomeAccount = thisIncome.getAccount();
        if (newAccount == null && requestTransactionDto.getAmount() != null) {
            if (transactionsCommonFunctionsUtil
                    .isSufficientAmount(
                            thisIncomeAccount, thisIncome.getAmount()) < 0) {
                throw new TransactionFailedException("Not enough money for transaction");
            }
            thisIncomeAccount.setBalance(thisIncomeAccount.getBalance()
                    .subtract(thisIncome.getAmount()));
            thisIncomeAccount.setBalance(thisIncomeAccount.getBalance()
                    .add(requestTransactionDto.getAmount()));
            accountRepository.save(thisIncomeAccount);
            thisIncome.setAmount(requestTransactionDto.getAmount());

            //Change account, but not the sum
        } else if (newAccount != null && requestTransactionDto.getAmount() == null) {
            if (transactionsCommonFunctionsUtil
                    .isSufficientAmount(thisIncomeAccount, thisIncome.getAmount()) < 0) {
                throw new TransactionFailedException("Not enough money for transaction");
            }
            thisIncomeAccount.setBalance(thisIncomeAccount.getBalance()
                    .subtract(thisIncome.getAmount()));
            newAccount.setBalance(thisIncomeAccount.getBalance()
                    .add(thisIncome.getAmount()));
            accountRepository.save(thisIncomeAccount);
            accountRepository.save(newAccount);
            thisIncome.setAccount(newAccount);
            thisIncome.setCurrency(newAccount.getCurrency());

            //Change both
        } else if (newAccount != null) {
            if (transactionsCommonFunctionsUtil
                    .isSufficientAmount(thisIncomeAccount, requestTransactionDto.getAmount()) < 0) {
                throw new TransactionFailedException("Not enough money for transaction");
            }
            thisIncomeAccount.setBalance(thisIncomeAccount.getBalance()
                    .subtract(thisIncome.getAmount()));
            newAccount.setBalance(thisIncomeAccount.getBalance()
                    .add(requestTransactionDto.getAmount()));
            accountRepository.save(thisIncomeAccount);
            accountRepository.save(newAccount);
            thisIncome.setAccount(newAccount);
            thisIncome.setAmount(requestTransactionDto.getAmount());
            thisIncome.setCurrency(newAccount.getCurrency());
        }
        if (requestTransactionDto.getTransactionDate() != null) {
            thisIncome.setTransactionDate(
                    LocalDate.parse(requestTransactionDto.getTransactionDate()));
        }
        if (requestTransactionDto.getComment() != null) {
            thisIncome.setComment(requestTransactionDto.getComment());
        }
        incomeRepository.save(thisIncome);
        return transactionMapper.toPersistIncomeDto(thisIncome);
    }

    @Transactional
    @Override
    public void deleteByTransactionId(Long userId, Long transactionId) {
        Income income = incomeRepository
                .findByIdAndUserId(transactionId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No income with id "
                                + transactionId + " was found for user with id " + userId));
        if (income.getIncomeCategory().getName().equals(TARGET_INCOME_CATEGORY)) {
            throw new ConflictException("Can't modify incomes with "
                    + TARGET_INCOME_CATEGORY + " category");
        }
        Account account = accountRepository
                .findByIdAndUserId(income.getAccount().getId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + income.getAccount().getId()
                                + " was found for user with id " + userId));
        if (transactionsCommonFunctionsUtil.isSufficientAmount(account, income.getAmount()) < 0) {
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
                    resultMap.put("Sum", dailyTotal);
                    categoryMap.forEach((category, amount) ->
                            resultMap.put(category.getName(), amount));
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

    private void presenceCheck(Long userId, FilterTransactionByDaysDto filterTransactionsDto) {
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

    private IncomeCategory getNewCategoryIfValid(UpdateTransactionDto requestTransactionDto,
                                                 Long userId) {
        IncomeCategory newIncomeCategory = incomeCategoryRepository.findByIdAndUserId(
                        requestTransactionDto.getCategoryId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("No category with id "
                        + requestTransactionDto.getCategoryId()
                        + " was found for user with id " + userId));

        if (newIncomeCategory.getName().equals(TARGET_INCOME_CATEGORY)) {
            throw new ConflictException("Can't assign " + TARGET_INCOME_CATEGORY
                    + " inside an update");
        }
        return newIncomeCategory;
    }

    private Account getNewAccountIfValid(UpdateTransactionDto requestTransactionDto, Long userId) {
        return accountRepository
                .findByIdAndUserId(requestTransactionDto.getAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No account with id " + requestTransactionDto.getAccountId()
                                + " was found for user with id " + userId));
    }
}
