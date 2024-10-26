package com.example.budgetingapp.dtos.transactions.request;

import java.math.BigDecimal;

public record RequestTransactionDto(String comment,
                                    BigDecimal amount,
                                    String transactionDate, //TODO mark it down somehow
                                    Long accountId,
                                    Long categoryId){}
