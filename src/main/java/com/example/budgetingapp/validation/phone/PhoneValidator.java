package com.example.budgetingapp.validation.phone;

import static com.example.budgetingapp.constants.validation.ValidationConstants.PATTERN_OF_PHONE;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return phone != null && Pattern.compile(PATTERN_OF_PHONE).matcher(phone).matches();
    }
}
