package com.example.budgetingapp.dtos.transfers.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferResponseDto {
    private Long id;
    private String comment;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private Long fromAccountId;
    private Long toAccountId;
}
