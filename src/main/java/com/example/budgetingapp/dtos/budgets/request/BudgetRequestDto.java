package com.example.budgetingapp.dtos.budgets.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.validation.date.Date;
import com.example.budgetingapp.validation.date.todateafterfromdate.BudgetToDateAfterFromDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@BudgetToDateAfterFromDate
public record BudgetRequestDto(
        @Schema(name = "name",
                example = "New budget",
                requiredMode = REQUIRED)
        @NotBlank
        String name,
        @Schema(name = "currency",
                example = "USD",
                requiredMode = REQUIRED)
        @NotBlank
        String currency,
        @Schema(name = "fromDate",
                example = "2024-10-29",
                description = "date format should be: YYYY-MM-dd",
                requiredMode = REQUIRED)
        @NotBlank
        @Date
        String fromDate,
        @Schema(name = "toDate",
                example = "2024-10-31",
                description = "date format should be: YYYY-MM-dd",
                requiredMode = REQUIRED)
        @NotBlank
        @Date
        String toDate,
        @Schema(name = "categoryId",
                example = "1",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long categoryId,
        @Schema(name = "limitSum",
                example = "999.99",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 2)
        BigDecimal limitSum) {}
