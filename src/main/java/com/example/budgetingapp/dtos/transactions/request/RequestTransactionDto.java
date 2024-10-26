package com.example.budgetingapp.dtos.transactions.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RequestTransactionDto(String comment,
                                    Long accountId,
                                    BigDecimal amount,
                                    LocalDate transactionDate,
                                    Long expenseCategoryId){}
