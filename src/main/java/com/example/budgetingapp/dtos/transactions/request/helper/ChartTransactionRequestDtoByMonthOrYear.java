package com.example.budgetingapp.dtos.transactions.request.helper;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import com.example.budgetingapp.validation.filtertype.ValidFilterType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChartTransactionRequestDtoByMonthOrYear(
        @Schema(name = TransactionDtoConstants.ACCOUNT_ID,
                example = TransactionDtoConstants.ACCOUNT_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Digits(integer = 9, fraction = 0)
        @Positive
        Long accountId,
        @Schema(name = TransactionDtoConstants.CATEGORY_IDS,
                example = TransactionDtoConstants.CATEGORY_IDS_EXAMPLE)
        Set<Long> categoryIds,
        @Schema(name = TransactionDtoConstants.FILTER,
                example = TransactionDtoConstants.FILTER_EXAMPLE)
        @ValidFilterType
        String filterType) {
}
