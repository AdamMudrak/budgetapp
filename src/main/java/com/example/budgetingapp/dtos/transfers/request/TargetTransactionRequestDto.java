package com.example.budgetingapp.dtos.transfers.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TargetTransactionRequestDto(String name,
                                          BigDecimal expectedSum,
                                          LocalDate achievedBefore,
                                          String currency){}
