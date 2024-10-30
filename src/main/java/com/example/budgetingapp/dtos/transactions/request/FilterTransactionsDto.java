package com.example.budgetingapp.dtos.transactions.request;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterTransactionsDto {
    @Schema(name = TransactionDtoConstants.ACCOUNT_ID,
            example = TransactionDtoConstants.ACCOUNT_ID_EXAMPLE)
    private String accountId;
    @Schema(name = TransactionDtoConstants.FROM_DATE,
            example = TransactionDtoConstants.FROM_DATE_EXAMPLE,
            description = TransactionDtoConstants.DATE_DESCRIPTION)
    private String fromDate;
    @Schema(name = TransactionDtoConstants.TO_DATE,
            example = TransactionDtoConstants.TO_DATE_EXAMPLE,
            description = TransactionDtoConstants.DATE_DESCRIPTION)
    private String toDate;
    @Schema(name = TransactionDtoConstants.CATEGORY_IDS,
            example = TransactionDtoConstants.CATEGORY_IDS_EXAMPLE)
    private String[] categoryIds;
}
