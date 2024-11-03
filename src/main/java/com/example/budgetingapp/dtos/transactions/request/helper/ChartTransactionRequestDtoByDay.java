package com.example.budgetingapp.dtos.transactions.request.helper;

import com.example.budgetingapp.validation.date.Date;
import com.example.budgetingapp.validation.date.ToDateAfterFromDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToDateAfterFromDate
public record ChartTransactionRequestDtoByDay(
        @Date
        String fromDate,
        @Date
        String toDate) {
}
