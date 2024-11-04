package com.example.budgetingapp.validation.filtertype;

import static com.example.budgetingapp.constants.Constants.MONTH;
import static com.example.budgetingapp.constants.Constants.YEAR;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidFilterTypeValidator implements ConstraintValidator<ValidFilterType, String> {
    @Override
    public boolean isValid(String filter, ConstraintValidatorContext constraintValidatorContext) {
        return filter.equals(MONTH) || filter.equals(YEAR);
    }
}
