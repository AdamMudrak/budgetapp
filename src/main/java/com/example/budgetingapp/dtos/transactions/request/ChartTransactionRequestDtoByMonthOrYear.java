package com.example.budgetingapp.dtos.transactions.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChartTransactionRequestDtoByMonthOrYear {
    private String filterType;
}
