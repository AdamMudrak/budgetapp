package com.example.budgetingapp.validation.fieldmatch;

import com.example.budgetingapp.dtos.user.request.UserSetNewPasswordRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldCurrentAndNewPasswordCollisionValidator implements ConstraintValidator<
        FieldSetNewPasswordMatch, UserSetNewPasswordRequestDto> {
    @Override
    public boolean isValid(UserSetNewPasswordRequestDto userSetNewPasswordRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (userSetNewPasswordRequestDto.currentPassword() == null
                || userSetNewPasswordRequestDto.newPassword() == null) {
            return false;
        }
        return !userSetNewPasswordRequestDto.currentPassword()
                .equals(userSetNewPasswordRequestDto.newPassword());
    }
}
