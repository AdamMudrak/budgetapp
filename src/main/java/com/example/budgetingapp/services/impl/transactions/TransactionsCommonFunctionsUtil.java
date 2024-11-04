package com.example.budgetingapp.services.impl.transactions;

import static com.example.budgetingapp.constants.Constants.MONTH;
import static com.example.budgetingapp.constants.Constants.YEAR;

import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByDay;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByMonthOrYear;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transactions.Income;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class TransactionsCommonFunctionsUtil {
    private static final int FIRST_DAY = 1;

    int isSufficientAmount(Account account, RequestTransactionDto requestTransactionDto) {
        return (account.getBalance()
                .subtract(requestTransactionDto.amount()))
                .compareTo(BigDecimal.ZERO);
    }

    int isSufficientAmount(Account account, Income income) {
        return (account.getBalance()
                .subtract(income.getAmount()))
                .compareTo(BigDecimal.ZERO);
    }

    boolean isDateWithinPeriod(LocalDate checkDate,
                               ChartTransactionRequestDtoByDay accumulatedTransactionRequestDto) {
        if (accumulatedTransactionRequestDto.fromDate() != null
                && accumulatedTransactionRequestDto.toDate() != null) {
            LocalDate fromDate = LocalDate.parse(accumulatedTransactionRequestDto.fromDate());
            LocalDate toDate = LocalDate.parse(accumulatedTransactionRequestDto.toDate());
            return ((checkDate.isAfter(fromDate)
                    || checkDate.isEqual(fromDate)
                    && (checkDate.isBefore(toDate)
                    || checkDate.isEqual(toDate))));
        }
        return false;
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
}
