package com.example.budgetingapp.dtos.transactions.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResponseTransactionDto(
        Long id,
        String comment,
        BigDecimal amount,
        LocalDate transactionDate,
        Long accountId,
        Long categoryId
) {
}
