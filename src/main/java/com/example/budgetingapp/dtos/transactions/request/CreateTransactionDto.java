package com.example.budgetingapp.dtos.transactions.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import com.example.budgetingapp.validation.date.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateTransactionDto(
        @Schema(name = TransactionDtoConstants.COMMENT,
                example = TransactionDtoConstants.COMMENT_EXAMPLE)
        String comment,
        @Schema(name = TransactionDtoConstants.AMOUNT,
                example = TransactionDtoConstants.AMOUNT_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 2)
        BigDecimal amount,
        @Schema(name = TransactionDtoConstants.DATE,
                example = TransactionDtoConstants.DATE_EXAMPLE,
                requiredMode = REQUIRED)
        @Date
        String transactionDate,
        @Schema(name = TransactionDtoConstants.ACCOUNT_ID,
                example = TransactionDtoConstants.ACCOUNT_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long accountId,
        @Schema(name = TransactionDtoConstants.CATEGORY_ID,
                example = TransactionDtoConstants.CATEGORY_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long categoryId) {
}
