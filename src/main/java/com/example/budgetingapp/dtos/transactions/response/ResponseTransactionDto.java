package com.example.budgetingapp.dtos.transactions.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseTransactionDto {
    private Long id;
    private String comment;
    private BigDecimal amount;
    private LocalDate transactionDate;
}
