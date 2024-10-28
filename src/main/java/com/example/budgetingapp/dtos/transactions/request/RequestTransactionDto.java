package com.example.budgetingapp.dtos.transactions.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestTransactionDto {
    private String comment;
    private BigDecimal amount;
    private String transactionDate;
    private Long accountId;
    private Long categoryId;
}
