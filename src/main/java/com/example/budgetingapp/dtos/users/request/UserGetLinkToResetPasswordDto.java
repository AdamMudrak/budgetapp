package com.example.budgetingapp.dtos.users.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.validation.email.Email;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserGetLinkToResetPasswordDto(
        @Schema(name = "email",
                example = "example@gmail.com",
                requiredMode = REQUIRED)
        @NotBlank
        @Email
        String email){}
