package com.example.budgetingapp.dtos.users.request;

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
public record SetNewPasswordDto(
        @Schema(name = "currentPassword",
                example = "Best_Password1@3$",
                description = UserDtoConstants.PASSWORD_DESCRIPTION,
                requiredMode = REQUIRED)
        @NotBlank
        String currentPassword,
        @Schema(name = "newPassword",
                example = "Best_Password1@3$",
                description = UserDtoConstants.PASSWORD_DESCRIPTION,
                requiredMode = REQUIRED)
        @Size(min = 8,
                max = 32)
        @NotBlank
        @Password
        String newPassword,
        @Schema(name = "repeatNewPassword",
                example = "Best_Password1@3$",
                description = "This field must be the same as password!",
                requiredMode = REQUIRED)
        @NotBlank
        String repeatNewPassword) {}
