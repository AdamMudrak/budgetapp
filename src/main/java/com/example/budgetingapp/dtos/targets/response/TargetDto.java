package com.example.budgetingapp.dtos.targets.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetDto {
    private Long id;
    private String name;
    private BigDecimal expectedSum;
    private BigDecimal currentSum;
    private BigDecimal downPayment;
    private LocalDate achievedBefore;
    private String periodLeft;
    private String currency;
    private boolean achieved;
}
