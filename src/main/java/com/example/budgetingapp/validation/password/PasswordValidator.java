package com.example.budgetingapp.validation.password;

import static com.example.budgetingapp.constants.validation.ValidationConstants.PASSWORD_PATTERN;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password != null && Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }
}
