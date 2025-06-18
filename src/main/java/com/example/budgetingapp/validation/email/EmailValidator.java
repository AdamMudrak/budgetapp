package com.example.budgetingapp.validation.email;

import static com.example.budgetingapp.constants.validation.ValidationConstants.PATTERN_OF_EMAIL;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email != null && PATTERN_OF_EMAIL.matcher(email).matches();
    }
}
