package com.example.budgetingapp.validation.date.todateafterfromdate;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilterToDateAfterFromDateValidator
        implements ConstraintValidator<FilterToDateAfterFromDate, FilterTransactionsDto> {
    @Override
    public boolean isValid(FilterTransactionsDto requestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return (requestDto.toDate() == null || requestDto.fromDate() == null)
                || LocalDate.parse(requestDto.toDate())
                .isAfter(LocalDate.parse(requestDto.fromDate()));
    }
}
