package com.example.budgetingapp.validation.date;

import static com.example.budgetingapp.constants.validation.ValidationConstants.DATE_PATTERN;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<Date, String> {
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        return date == null || DATE_PATTERN.matcher(date).matches();
    }
}
