package com.example.budgetingapp.dtos.transactions.response;

import java.time.LocalDate;
import java.util.List;

public record AccumulatedResultDto(LocalDate localDate,
                                   List<TransactionSumByCategoryDto> sumsByCategory) {}
