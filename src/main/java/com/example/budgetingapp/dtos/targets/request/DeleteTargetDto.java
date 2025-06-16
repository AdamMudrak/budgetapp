package com.example.budgetingapp.dtos.targets.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeleteTargetDto(
        @Schema(name = "targetId",
                example = "1",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long targetId,
        @Schema(name = "accountId",
                example = "1",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long accountId) {
}
