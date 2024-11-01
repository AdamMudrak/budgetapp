package com.example.budgetingapp.dtos.budget.request;

import com.example.budgetingapp.validation.date.Date;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;

public record BudgetRequestDto(
        @NotBlank
        String name,
        @NotBlank
        @Date
        String fromDate,
        @NotBlank
        @Date
        String toDate,
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 0)
        Set<Long> categoryIds,
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 2)
        BigDecimal limitSum) {}
