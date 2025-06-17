package com.example.budgetingapp.dtos.transactions.request.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FilterTransactionByMonthsYearsDto(
        Long accountId,
        Set<Long> categoryIds,
        FilterType filterType) {
}
