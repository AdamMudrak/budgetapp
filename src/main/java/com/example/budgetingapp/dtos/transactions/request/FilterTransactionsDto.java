package com.example.budgetingapp.dtos.transactions.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterTransactionsDto {
    private String accountId;
    private String fromDate;
    private String toDate;
    private String[] categoryIds;
}
