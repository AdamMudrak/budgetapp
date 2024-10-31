package com.example.budgetingapp.dtos.transfers.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeleteTargetRequestDto(
        @Schema(name = TransactionDtoConstants.TARGET_ID,
                example = TransactionDtoConstants.TARGET_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 0)
        Long targetId,
        @Schema(name = TransactionDtoConstants.ACCOUNT_ID,
                example = TransactionDtoConstants.ACCOUNT_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 0)
        Long accountId) {
}
