package com.example.budgetingapp.services.implementations.transactions;

import static com.example.budgetingapp.constants.Constants.MONTH;
import static com.example.budgetingapp.constants.Constants.NO_VALUE;
import static com.example.budgetingapp.constants.Constants.YEAR;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateRequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByMonthOrYear;
import com.example.budgetingapp.dtos.transactions.response.ChartsAccumulatedResultDto;
import com.example.budgetingapp.dtos.transactions.response.helper.TransactionSumByCategoryDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transactions.Income;
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

    int isSufficientAmount(Account account, RequestTransactionDto requestTransactionDto) {
        return (account.getBalance()
                .subtract(requestTransactionDto.amount()))
                .compareTo(BigDecimal.ZERO);
    }

    int isSufficientAmount(Account account, UpdateRequestTransactionDto requestTransactionDto) {
        return (account.getBalance()
                .subtract(requestTransactionDto.amount()))
                .compareTo(BigDecimal.ZERO);
    }

    int isSufficientAmount(Account account, Income income) {
        return (account.getBalance()
                .subtract(income.getAmount()))
                .compareTo(BigDecimal.ZERO);
    }

    LocalDate getPeriodDate(LocalDate transactionDate,
                ChartTransactionRequestDtoByMonthOrYear chartTransactionRequestDtoByMonthOrYear) {
        return switch (chartTransactionRequestDtoByMonthOrYear.filterType()) {
            case MONTH -> transactionDate.withDayOfMonth(FIRST_DAY);
            case YEAR -> transactionDate.withDayOfYear(FIRST_DAY);
            default -> throw new IllegalArgumentException(
                    "Unexpected value: "
                            + chartTransactionRequestDtoByMonthOrYear.filterType());
        };
    }

    FilterTransactionsDto getFilterDtoWithNoDates(
            ChartTransactionRequestDtoByMonthOrYear chartTransactionRequestDtoByMonthOrYear) {
        return new FilterTransactionsDto(
                chartTransactionRequestDtoByMonthOrYear.accountId(),
                chartTransactionRequestDtoByMonthOrYear.categoryIds(),
                NO_VALUE, NO_VALUE);
    }

    List<ChartsAccumulatedResultDto> prepareListOfAccumulatedDtos(
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
                    return new ChartsAccumulatedResultDto(entry.getKey(), sumsByDate);
                })
                .sorted(Comparator.comparing(ChartsAccumulatedResultDto::localDate))
                .collect(Collectors.toList());
    }
}
