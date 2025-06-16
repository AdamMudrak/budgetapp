package com.example.budgetingapp.dtos.accounts.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateAccountDto(
        @Schema(name = "name",
                example = "credit card",
                requiredMode = REQUIRED)
        @NotBlank
        String name,
        @Schema(name = "balance",
                example = "9999.99",
                description = "Maximum value is 999999999.99",
                requiredMode = REQUIRED)
        @NotNull
        @Min(0)
        @Digits(integer = 9, fraction = 2)
        BigDecimal balance,
        @Schema(name = "currency",
                example = "USD",
                requiredMode = REQUIRED)
        @NotBlank
        String currency){}
