package com.example.budgetingapp.validation.date.todateafterfromdate;

import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BudgetToDateAfterFromDateValidator
        implements ConstraintValidator<BudgetToDateAfterFromDate, BudgetRequestDto> {
    @Override
    public boolean isValid(BudgetRequestDto requestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return LocalDate.parse(requestDto.toDate())
                .isAfter(LocalDate.parse(requestDto.fromDate()));
    }
}
