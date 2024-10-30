package com.example.budgetingapp.dtos.transactions.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TargetTransactionResponseDto(Long id,
                                           String name,
                                           BigDecimal expectedSum,
                                           LocalDate achievedBefore) {
}
