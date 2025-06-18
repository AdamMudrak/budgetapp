package com.example.budgetingapp.validation.fieldmatch;

import com.example.budgetingapp.dtos.users.request.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldRegisterMatchValidator implements ConstraintValidator<FieldRegisterMatch,
        UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto userRegistrationRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return userRegistrationRequestDto.password() != null
                && userRegistrationRequestDto.repeatPassword() != null
                && userRegistrationRequestDto.password()
                .equals(userRegistrationRequestDto.repeatPassword());
    }
}
