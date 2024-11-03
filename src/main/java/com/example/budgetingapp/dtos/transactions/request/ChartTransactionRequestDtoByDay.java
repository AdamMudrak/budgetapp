package com.example.budgetingapp.dtos.transactions.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChartTransactionRequestDtoByDay {
    private LocalDate fromDate;
    private LocalDate toDate;
}
