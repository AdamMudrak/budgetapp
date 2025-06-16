package com.example.budgetingapp.dtos.support.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SupportRequestDto(
        @Schema(name = "name",
                example = "Adam",
                requiredMode = REQUIRED)
        @NotBlank
        String name,
        @Schema(name = "email",
                example = "example@gmail.com",
                requiredMode = REQUIRED)
        @NotBlank
        @Email
        String email,
        @Schema(name = "message",
                example = "Good day! I'd like to find out more about...",
                requiredMode = REQUIRED)
        @NotBlank
        String message) {
}
