package com.example.budgetingapp.dtos.transfers.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferRequestDto {
    private String comment;
    private BigDecimal amount;
    private String transactionDate;
    private Long fromAccountId;
    private Long toAccountId;
}
