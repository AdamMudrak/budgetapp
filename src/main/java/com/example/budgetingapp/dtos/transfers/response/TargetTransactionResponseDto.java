package com.example.budgetingapp.dtos.transfers.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TargetTransactionResponseDto(Long id,
                                           String name,
                                           BigDecimal expectedSum,
                                           BigDecimal currentSum,
                                           BigDecimal monthlyDownPayment,
                                           LocalDate achievedBefore,
                                           String currency,
                                           boolean achieved) {
}
