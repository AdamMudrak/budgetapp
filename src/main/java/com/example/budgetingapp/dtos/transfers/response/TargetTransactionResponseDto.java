package com.example.budgetingapp.dtos.transfers.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetTransactionResponseDto {
    private Long id;
    private String name;
    private BigDecimal expectedSum;
    private BigDecimal currentSum;
    private BigDecimal monthlyDownPayment;
    private LocalDate achievedBefore;
    private Long monthLeft;
    private String currency;
    private boolean achieved;
}
