package com.example.budgetingapp.dtos.transactions.response.charts.inner;

import java.math.BigDecimal;

public record SumsByCategoryDto(
        String sumByDateOrCategory, BigDecimal sum) {}
