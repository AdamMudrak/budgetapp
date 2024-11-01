package com.example.budgetingapp.dtos.budget.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetResponseDto {
    private String name;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Set<Long> categoryIds;
    private BigDecimal limitSum;
    private BigDecimal currentSum;
    private boolean isExceed;
}
