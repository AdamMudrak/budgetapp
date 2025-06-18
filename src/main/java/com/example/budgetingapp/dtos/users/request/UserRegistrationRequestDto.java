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
        @JsonAlias("email")
        @Schema(name = "email",
        example = "example@gmail.com",
        requiredMode = REQUIRED)
        @NotBlank
        @Email String userName,

        @Schema(name = "password",
        example = "Best_Password1@3$",
        description = UserDtoConstants.PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @Size(min = 8,
                max = 32)
        @NotBlank
        @Password String password,

        @Schema(name = "repeatPassword",
        example = "Best_Password1@3$",
        description = "This field must be the same as password!",
        requiredMode = REQUIRED)
        @NotBlank String repeatPassword) {}
