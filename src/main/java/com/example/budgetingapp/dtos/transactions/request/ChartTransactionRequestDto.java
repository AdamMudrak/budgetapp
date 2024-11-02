package com.example.budgetingapp.dtos.transactions.request;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartTransactionRequestDto {
    private FilterType filterType;
    private LocalDate fromDate;
    private LocalDate toDate;
}
