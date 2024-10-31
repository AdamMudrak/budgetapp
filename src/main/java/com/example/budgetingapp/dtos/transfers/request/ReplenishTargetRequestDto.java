package com.example.budgetingapp.dtos.transfers.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ReplenishTargetRequestDto(
        @Schema(name = TransactionDtoConstants.FROM_ACCOUNT_ID,
                example = TransactionDtoConstants.FROM_ACCOUNT_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 0)
        Long fromAccountId,
        @Schema(name = TransactionDtoConstants.TO_TARGET_ID,
                example = TransactionDtoConstants.TO_TARGET_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 0)
        Long toTargetId,
        @Schema(name = TransactionDtoConstants.SUM_OF_REPLENISHMENT,
                example = TransactionDtoConstants.SUM_OF_REPLENISHMENT_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 2)
        BigDecimal sumOfReplenishment) {
}