package com.example.budgetingapp.dtos.transactions.request.helper;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChartTransactionRequestDtoByMonthOrYear(
        @Schema(name = TransactionDtoConstants.FILTER,
                example = TransactionDtoConstants.FILTER_EXAMPLE)
        String filterType) {
}
