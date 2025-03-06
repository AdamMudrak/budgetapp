package com.example.budgetingapp.dtos.targets.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeleteTargetDto(
        @Schema(name = TransactionDtoConstants.TARGET_ID,
                example = TransactionDtoConstants.TARGET_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long targetId,
        @Schema(name = TransactionDtoConstants.ACCOUNT_ID,
                example = TransactionDtoConstants.ACCOUNT_ID_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long accountId) {
}
