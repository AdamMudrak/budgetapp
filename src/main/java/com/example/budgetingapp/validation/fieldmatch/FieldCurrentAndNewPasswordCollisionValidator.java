package com.example.budgetingapp.validation.fieldmatch;

import com.example.budgetingapp.dtos.users.request.SetNewPasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldCurrentAndNewPasswordCollisionValidator implements ConstraintValidator<
        FieldCurrentAndNewPasswordCollision, SetNewPasswordDto> {
    @Override
    public boolean isValid(SetNewPasswordDto userSetNewPasswordRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return userSetNewPasswordRequestDto.currentPassword() != null
                && userSetNewPasswordRequestDto.newPassword() != null
                && !userSetNewPasswordRequestDto.currentPassword()
                .equals(userSetNewPasswordRequestDto.newPassword());
    }
}
