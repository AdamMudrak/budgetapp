package com.example.budgetingapp.dtos.transactions.response;

import com.example.budgetingapp.dtos.categories.response.ResponseCategoryDto;
import java.math.BigDecimal;

public record TransactionSumByCategoryDto(
        ResponseCategoryDto responseCategoryDto, BigDecimal sum) {}
