package com.example.budgetingapp.dtos.targets.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.validation.date.Date;
import com.example.budgetingapp.validation.date.DateAfterToday;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateTargetDto(
        @Schema(name = "name",
                example = "new target",
                requiredMode = REQUIRED)
        @NotBlank
        String name,
        @Schema(name = "expectedSum",
                example = "999.99",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 2)
        BigDecimal expectedSum,
        @Schema(name = "achievedBefore",
                example = "2040-10-29",
                requiredMode = REQUIRED)
        @Date
        @DateAfterToday
        @NotBlank
        String achievedBefore,
        @Schema(name = "currency",
                example = "USD",
                requiredMode = REQUIRED)
        @NotBlank
        String currency){}
