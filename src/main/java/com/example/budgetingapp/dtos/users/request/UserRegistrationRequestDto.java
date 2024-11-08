package com.example.budgetingapp.dtos.users.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.UserDtoConstants;
import com.example.budgetingapp.validation.email.Email;
import com.example.budgetingapp.validation.fieldmatch.FieldRegisterMatch;
import com.example.budgetingapp.validation.password.Password;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldRegisterMatch
public record UserRegistrationRequestDto(
        @JsonAlias(UserDtoConstants.EMAIL)
        @Schema(name = UserDtoConstants.EMAIL,
        example = UserDtoConstants.EMAIL_EXAMPLE,
        requiredMode = REQUIRED)
        @NotBlank
        @Email String userName,

        @Schema(name = UserDtoConstants.PASSWORD,
        example = UserDtoConstants.PASSWORD_EXAMPLE,
        description = UserDtoConstants.PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @Size(min = UserDtoConstants.MIN_PASSWORD_SIZE,
                max = UserDtoConstants.MAX_PASSWORD_SIZE)
        @NotBlank
        @Password String password,

        @Schema(name = UserDtoConstants.REPEAT_PASSWORD,
        example = UserDtoConstants.PASSWORD_EXAMPLE,
        description = UserDtoConstants.REPEAT_PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @NotBlank String repeatPassword) {}
