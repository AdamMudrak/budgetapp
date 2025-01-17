package com.example.budgetingapp.validation.date.todateafterfromdate;

import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilterToDateAfterFromDateValidator
        implements ConstraintValidator<FilterToDateAfterFromDate, FilterTransactionByDaysDto> {
    @Override
    public boolean isValid(FilterTransactionByDaysDto requestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return (requestDto.toDate() == null || requestDto.fromDate() == null)
                || LocalDate.parse(requestDto.toDate())
                .isAfter(LocalDate.parse(requestDto.fromDate()));
    }
}
