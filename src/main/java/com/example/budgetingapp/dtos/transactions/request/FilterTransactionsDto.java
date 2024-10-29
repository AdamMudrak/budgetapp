package com.example.budgetingapp.dtos.transactions.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterTransactionsDto {
    private Long accountId;
    private LocalDate from;
    private LocalDate to;
    private Set<Long> categoryIds;
}
