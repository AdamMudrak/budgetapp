package com.example.budgetingapp.dtos.transactions.response;

import java.math.BigDecimal;

public record TransactionSumByCategoryDto(
        String sumByDateOrCategory, BigDecimal sum) {}
