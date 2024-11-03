package com.example.budgetingapp.validation.date;

import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByDay;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ToDateAfterFromDateValidator
        implements ConstraintValidator<ToDateAfterFromDate, ChartTransactionRequestDtoByDay> {
    @Override
    public boolean isValid(ChartTransactionRequestDtoByDay requestDtoByDay,
                           ConstraintValidatorContext constraintValidatorContext) {
        return LocalDate.parse(requestDtoByDay.toDate())
                .isAfter(LocalDate.parse(requestDtoByDay.fromDate()));
    }
}
