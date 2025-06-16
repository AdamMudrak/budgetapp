package com.example.budgetingapp.dtos.transactions.request.filters;

import com.example.budgetingapp.validation.date.Date;
import com.example.budgetingapp.validation.date.todateafterfromdate.FilterToDateAfterFromDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@FilterToDateAfterFromDate
public record FilterTransactionByDaysDto(
        @Schema(name = "accountId",
                example = "1")
        Long accountId,
        @Schema(name = "categoryIds",
                example = "[1, 2]")
        Set<Long> categoryIds,
        @Schema(name = "fromDate",
                example = "2024-10-29",
                description = "date format should be: YYYY-MM-dd")
        @Date
        String fromDate,
        @Schema(name = "toDate",
                example = "2024-10-31",
                description = "date format should be: YYYY-MM-dd")
        @Date
        String toDate) {
}
