package com.example.budgetingapp.services.impl.transactions;

import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByMonthsYearsDto;
import com.example.budgetingapp.dtos.transactions.response.charts.SumsByPeriodDto;
import com.example.budgetingapp.dtos.transactions.response.charts.inner.SumsByCategoryDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.repositories.AccountRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TransactionsCommonFunctionsUtil {
    private static final int FIRST_DAY = 1;

    public int isSufficientAmount(Account account, BigDecimal newAmount) {
        return (account.getBalance()
                .subtract(newAmount))
                .compareTo(BigDecimal.ZERO);
    }

    LocalDate getPeriodDate(LocalDate transactionDate,
                FilterTransactionByMonthsYearsDto chartTransactionRequestDtoByMonthOrYear) {
        return switch (chartTransactionRequestDtoByMonthOrYear.filterType()) {
            case MONTH -> transactionDate.withDayOfMonth(FIRST_DAY);
            case YEAR -> transactionDate.withDayOfYear(FIRST_DAY);
        };
    }

    FilterTransactionByDaysDto getFilterDtoWithNoDates(
            FilterTransactionByMonthsYearsDto chartTransactionRequestDtoByMonthOrYear) {
        return new FilterTransactionByDaysDto(
                chartTransactionRequestDtoByMonthOrYear.accountId(),
                chartTransactionRequestDtoByMonthOrYear.categoryIds(),
                null,
                null);
    }

    List<SumsByPeriodDto> prepareListOfAccumulatedDtos(
            Map<LocalDate, Map<String, BigDecimal>> categorizedExpenseSums) {
        return categorizedExpenseSums.entrySet().stream()
                .map(entry -> {
                    List<SumsByCategoryDto> sumsByDate = entry
                            .getValue()
                            .entrySet()
                            .stream()
                            .map(dateEntry -> new SumsByCategoryDto(
                                    dateEntry.getKey(),
                                    dateEntry.getValue()))
                            .collect(Collectors.toList());
                    return new SumsByPeriodDto(entry.getKey(), sumsByDate);
                })
                .sorted(Comparator.comparing(SumsByPeriodDto::localDate))
                .collect(Collectors.toList());
    }

    void accountPresenceCheck(Long userId,
                              FilterTransactionByDaysDto filterTransactionsDto,
                              AccountRepository accountRepository) {
        if (filterTransactionsDto.accountId() != null) {
            if (!accountRepository.existsByIdAndUserId(filterTransactionsDto.accountId(), userId)) {
                throw new EntityNotFoundException("No account with id "
                        + filterTransactionsDto.accountId() + " for user with id "
                        + userId + " was found");
            }
        }
    }
}
