package com.example.budgetingapp.dtos.transactions.response.helper;

import java.math.BigDecimal;

public record TransactionSumByCategoryDto(
        String sumByDateOrCategory, BigDecimal sum) {}
