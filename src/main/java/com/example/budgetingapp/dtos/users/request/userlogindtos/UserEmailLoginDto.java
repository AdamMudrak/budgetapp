package com.example.budgetingapp.dtos.users.request.userlogindtos;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.UserDtoConstants;
import com.example.budgetingapp.validation.email.Email;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserEmailLoginDto(
        @Schema(name = "email",
        example = "example@gmail.com",
        requiredMode = REQUIRED)
        @NotBlank
        @Email
        String email,

        @Schema(name = "password",
        example = "Best_Password1@3$",
        description = UserDtoConstants.PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @NotBlank
        String password) {
}
