package com.example.budgetingapp.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateAfterTodayValidator implements ConstraintValidator<DateAfterToday, String> {
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate now = LocalDate.now();
        return LocalDate.parse(date).isAfter(now);
    }
}
