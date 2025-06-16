package com.example.budgetingapp.dtos.transactions.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.validation.date.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateTransactionDto(
        @Schema(name = "comment",
                example = "new transaction")
        String comment,
        @Schema(name = "amount",
                example = "999.99",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 2)
        BigDecimal amount,
        @Schema(name = "transactionDate",
                example = "2024-10-29",
                requiredMode = REQUIRED)
        @Date
        String transactionDate,
        @Schema(name = "accountId",
                example = "1",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long accountId,
        @Schema(name = "categoryId",
                example = "1",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long categoryId) {
}
