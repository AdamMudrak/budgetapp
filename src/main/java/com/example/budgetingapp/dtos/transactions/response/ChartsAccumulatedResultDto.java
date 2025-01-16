package com.example.budgetingapp.dtos.transactions.response;

import com.example.budgetingapp.dtos.transactions.response.helper.TransactionSumByCategoryDto;
import java.time.LocalDate;
import java.util.List;

public record ChartsAccumulatedResultDto(LocalDate localDate,
                                         List<TransactionSumByCategoryDto> sumsByCategory) {}
