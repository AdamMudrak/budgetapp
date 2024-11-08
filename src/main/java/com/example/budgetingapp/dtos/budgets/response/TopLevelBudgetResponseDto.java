package com.example.budgetingapp.dtos.budgets.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopLevelBudgetResponseDto {
    private String name;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Set<Long> categoryIds;
    private BigDecimal limitSum;
    private BigDecimal currentSum;
    private boolean isExceeded;
}
