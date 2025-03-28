package com.example.budgetingapp.dtos.budgets.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.BudgetDtoConstants;
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
        @Schema(name = BudgetDtoConstants.NAME,
                example = BudgetDtoConstants.NAME_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String name,
        @Schema(name = BudgetDtoConstants.CURRENCY,
                example = BudgetDtoConstants.CURRENCY_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String currency,
        @Schema(name = BudgetDtoConstants.FROM_DATE,
                example = BudgetDtoConstants.FROM_DATE_EXAMPLE,
                description = BudgetDtoConstants.DATE_DESCRIPTION,
                requiredMode = REQUIRED)
        @NotBlank
        @Date
        String fromDate,
        @Schema(name = BudgetDtoConstants.TO_DATE,
                example = BudgetDtoConstants.TO_DATE_EXAMPLE,
                description = BudgetDtoConstants.DATE_DESCRIPTION,
                requiredMode = REQUIRED)
        @NotBlank
        @Date
        String toDate,
        @Schema(name = BudgetDtoConstants.CATEGORY_ID,
                example = BudgetDtoConstants.CATEGORY_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long categoryId,
        @Schema(name = BudgetDtoConstants.LIMIT_SUM,
                example = BudgetDtoConstants.LIMIT_SUM_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 2)
        BigDecimal limitSum) {}
