package com.example.budgetingapp.dtos.accounts.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.AccountDtoConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateAccountDto(
        @Schema(name = AccountDtoConstants.NAME,
                example = AccountDtoConstants.NAME_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String name,
        @Schema(name = AccountDtoConstants.BALANCE,
                example = AccountDtoConstants.BALANCE_EXAMPLE,
                description = AccountDtoConstants.BALANCE_DESCRIPTION,
                requiredMode = REQUIRED)
        @NotNull
        @Min(0)
        @Digits(integer = 9, fraction = 2)
        BigDecimal balance,
        @Schema(name = AccountDtoConstants.CURRENCY,
                example = AccountDtoConstants.CURRENCY_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String currency){}
