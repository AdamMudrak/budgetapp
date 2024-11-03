package com.example.budgetingapp.services.impl.transactions;

import com.example.budgetingapp.dtos.transactions.request.ChartTransactionRequestDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transactions.Income;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class TransactionsCommonFunctionsUtil {
    private static final int FIRST_DAY = 1;

    public int isSufficientAmount(Account account, RequestTransactionDto requestTransactionDto) {
        return (account.getBalance()
                .subtract(requestTransactionDto.amount()))
                .compareTo(BigDecimal.ZERO);
    }

    public int isSufficientAmount(Account account, Income income) {
        return (account.getBalance()
                .subtract(income.getAmount()))
                .compareTo(BigDecimal.ZERO);
    }

    public boolean isDateWithinPeriod(LocalDate checkDate,
                                   ChartTransactionRequestDto accumulatedTransactionRequestDto) {
        if (accumulatedTransactionRequestDto.getFromDate() == null
                && accumulatedTransactionRequestDto.getToDate() == null) {
            return true;
        }
        return ((checkDate.isAfter(accumulatedTransactionRequestDto.getFromDate())
                || checkDate.isEqual(accumulatedTransactionRequestDto.getFromDate()))
                && (checkDate.isBefore(accumulatedTransactionRequestDto.getToDate())
                || checkDate.isEqual(accumulatedTransactionRequestDto.getToDate())));
    }

    public LocalDate getPeriodDate(LocalDate transactionDate,
                                    ChartTransactionRequestDto chartTransactionRequestDto) {
        return switch (chartTransactionRequestDto.getFilterType()) {
            case DAY -> transactionDate;
            case MONTH -> transactionDate.withDayOfMonth(FIRST_DAY);
            case YEAR -> transactionDate.withDayOfYear(FIRST_DAY);
        };
    }
}
