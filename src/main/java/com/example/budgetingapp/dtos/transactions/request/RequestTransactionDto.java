package com.example.budgetingapp.dtos.transactions.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTransactionDto {
    private String comment;
    private BigDecimal amount;
    private String transactionDate;
    private Long accountId;
    private Long categoryId;
}
