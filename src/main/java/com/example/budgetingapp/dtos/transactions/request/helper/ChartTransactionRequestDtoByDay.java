package com.example.budgetingapp.dtos.transactions.request.helper;

import com.example.budgetingapp.validation.date.Date;
import com.example.budgetingapp.validation.date.todateafterfromdate.ChartToDateAfterFromDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@ChartToDateAfterFromDate
public record ChartTransactionRequestDtoByDay(
        @Date
        @NotBlank
        String fromDate,
        @Date
        @NotBlank
        String toDate) {
}
