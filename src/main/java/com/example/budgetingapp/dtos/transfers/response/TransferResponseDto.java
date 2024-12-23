package com.example.budgetingapp.dtos.transfers.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferResponseDto(
        Long id,
        String comment,
        String currency,
        BigDecimal amount,
        LocalDate transactionDate,
        Long fromAccountId,
        Long toAccountId) {
}
