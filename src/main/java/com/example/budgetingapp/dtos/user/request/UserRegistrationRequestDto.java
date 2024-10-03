package com.example.budgetingapp.dtos.user.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.UserDtoConstants;
import com.example.budgetingapp.validation.Email;
import com.example.budgetingapp.validation.FieldMatch;
import com.example.budgetingapp.validation.Password;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldMatch
public record UserRegistrationRequestDto(
        @Schema(name = UserDtoConstants.EMAIL,
        example = UserDtoConstants.EMAIL_EXAMPLE,
        requiredMode = REQUIRED)
        @NotBlank
        @Email String email,

        @Schema(name = UserDtoConstants.PASSWORD,
        example = UserDtoConstants.PASSWORD_EXAMPLE,
        description = UserDtoConstants.PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @Size(min = 8, max = 32)
        @NotBlank
        @Password String password,

        @Schema(name = UserDtoConstants.REPEAT_PASSWORD,
        example = UserDtoConstants.REPEAT_PASSWORD_EXAMPLE,
        description = UserDtoConstants.REPEAT_PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @NotBlank String repeatPassword,

        @Schema(name = UserDtoConstants.FIRST_NAME,
        example = UserDtoConstants.FIRST_NAME_EXAMPLE,
        requiredMode = REQUIRED)
        @NotBlank String firstName,

        @Schema(name = UserDtoConstants.LAST_NAME,
        example = UserDtoConstants.LAST_NAME_EXAMPLE,
        requiredMode = REQUIRED)
        @NotBlank String lastName){}
