package com.example.budgetingapp.dtos.transactions.request;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import com.example.budgetingapp.validation.date.Date;
import com.example.budgetingapp.validation.date.todateafterfromdate.FilterToDateAfterFromDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@FilterToDateAfterFromDate
public record FilterTransactionsDto(
        @Schema(name = TransactionDtoConstants.ACCOUNT_ID,
                example = TransactionDtoConstants.ACCOUNT_ID_EXAMPLE)
        Long accountId,
        @Schema(name = TransactionDtoConstants.CATEGORY_IDS,
                example = TransactionDtoConstants.CATEGORY_IDS_EXAMPLE)
        Set<Long> categoryIds,
        @Schema(name = TransactionDtoConstants.FROM_DATE,
                example = TransactionDtoConstants.FROM_DATE_EXAMPLE,
                description = TransactionDtoConstants.DATE_DESCRIPTION)
        @Date
        String fromDate,
        @Schema(name = TransactionDtoConstants.TO_DATE,
                example = TransactionDtoConstants.TO_DATE_EXAMPLE,
                description = TransactionDtoConstants.DATE_DESCRIPTION)
        @Date
        String toDate) {
}
