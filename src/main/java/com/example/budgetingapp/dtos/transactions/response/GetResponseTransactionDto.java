package com.example.budgetingapp.dtos.transactions.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GetResponseTransactionDto(
        Long id,
        String comment,
        String currency,
        BigDecimal amount,
        LocalDate transactionDate,
        Long accountId,
        String accountName,
        Long categoryId,
        String categoryName
) {
}
