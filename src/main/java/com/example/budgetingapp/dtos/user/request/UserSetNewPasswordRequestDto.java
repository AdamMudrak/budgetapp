package com.example.budgetingapp.dtos.user.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.UserDtoConstants;
import com.example.budgetingapp.validation.fieldmatch.FieldCurrentAndNewPasswordCollision;
import com.example.budgetingapp.validation.fieldmatch.FieldSetNewPasswordMatch;
import com.example.budgetingapp.validation.password.Password;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldSetNewPasswordMatch
@FieldCurrentAndNewPasswordCollision
public record UserSetNewPasswordRequestDto(
        @Schema(name = UserDtoConstants.CURRENT_PASSWORD,
                example = UserDtoConstants.PASSWORD_EXAMPLE,
                description = UserDtoConstants.PASSWORD_DESCRIPTION,
                requiredMode = REQUIRED)
        @Size(min = 8, max = 32)
        @NotBlank
        @Password
        String currentPassword,
        @Schema(name = UserDtoConstants.NEW_PASSWORD,
                example = UserDtoConstants.PASSWORD_EXAMPLE,
                description = UserDtoConstants.PASSWORD_DESCRIPTION,
                requiredMode = REQUIRED)
        @Size(min = 8, max = 32)
        @NotBlank
        @Password
        String newPassword,
        @Schema(name = UserDtoConstants.REPEAT_NEW_PASSWORD,
                example = UserDtoConstants.PASSWORD_EXAMPLE,
                description = UserDtoConstants.REPEAT_PASSWORD_DESCRIPTION,
                requiredMode = REQUIRED)
        @NotBlank
        String repeatNewPassword) {}
