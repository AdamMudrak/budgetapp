package com.example.budgetingapp.dtos.transactions.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaveAndUpdateResponseTransactionDto(
        Long id,
        String comment,
        String currency,
        BigDecimal amount,
        LocalDate transactionDate,
        Long accountId,
        Long categoryId) {
}
