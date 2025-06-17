package com.example.budgetingapp.dtos.transactions.request.filters;

import java.time.LocalDate;
import java.util.Set;

public record FilterTransactionByDaysDto(
        Long accountId,
        Set<Long> categoryIds,
        LocalDate fromDate,
        LocalDate toDate) {
}
