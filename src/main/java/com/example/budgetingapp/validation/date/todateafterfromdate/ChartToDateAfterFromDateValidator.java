package com.example.budgetingapp.validation.date.todateafterfromdate;

import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByDay;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ChartToDateAfterFromDateValidator
        implements ConstraintValidator<ChartToDateAfterFromDate, ChartTransactionRequestDtoByDay> {
    @Override
    public boolean isValid(ChartTransactionRequestDtoByDay requestDtoByDay,
                           ConstraintValidatorContext constraintValidatorContext) {
        return LocalDate.parse(requestDtoByDay.toDate())
                .isAfter(LocalDate.parse(requestDtoByDay.fromDate()));
    }
}
