package com.example.budgetingapp.dtos.account.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CreateAccountDto(
        @NotBlank
        String name,
        BigDecimal balance,
        @NotBlank
        String currency){}
