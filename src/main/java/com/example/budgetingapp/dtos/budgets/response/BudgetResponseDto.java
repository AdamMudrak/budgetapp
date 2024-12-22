package com.example.budgetingapp.dtos.budgets.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetResponseDto {
    private Long id;
    private String name;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long categoryId;
    private String currency;
    private BigDecimal limitSum;
    private BigDecimal currentSum;
    private boolean isExceeded;
}
