package com.example.budgetingapp.validation.fieldmatch;

import com.example.budgetingapp.dtos.users.request.SetNewPasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldSetNewPasswordMatchValidator implements ConstraintValidator<
        FieldSetNewPasswordMatch, SetNewPasswordDto> {
    @Override
    public boolean isValid(SetNewPasswordDto userSetNewPasswordRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (userSetNewPasswordRequestDto.newPassword() == null
                || userSetNewPasswordRequestDto.repeatNewPassword() == null) {
            return false;
        }
        return userSetNewPasswordRequestDto.newPassword()
                .equals(userSetNewPasswordRequestDto.repeatNewPassword());
    }
}
