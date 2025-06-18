package com.example.budgetingapp.dtos.targets.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReplenishTargetDto(
        @Schema(name = "fromAccountId",
                example = "1",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long fromAccountId,
        @Schema(name = "toTargetId",
                example = "1",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long toTargetId,
        @Schema(name = "sumOfReplenishment",
                example = "999.99",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 2)
        BigDecimal sumOfReplenishment) {
}
