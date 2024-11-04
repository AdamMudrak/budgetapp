package com.example.budgetingapp.dtos.transactions.request.helper;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import com.example.budgetingapp.validation.date.Date;
import com.example.budgetingapp.validation.date.todateafterfromdate.ChartToDateAfterFromDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@ChartToDateAfterFromDate
public record ChartTransactionRequestDtoByDay(
        @Schema(name = TransactionDtoConstants.FROM_DATE,
                example = TransactionDtoConstants.FROM_DATE_EXAMPLE,
                requiredMode = REQUIRED)
        @Date
        @NotBlank
        String fromDate,
        @Schema(name = TransactionDtoConstants.TO_DATE,
                example = TransactionDtoConstants.TO_DATE_EXAMPLE,
                requiredMode = REQUIRED)
        @Date
        @NotBlank
        String toDate) {
}
