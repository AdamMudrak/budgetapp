package com.example.budgetingapp.dtos.targets.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
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
public record TargetTransactionRequestDto(
        @Schema(name = TransactionDtoConstants.NAME,
                example = TransactionDtoConstants.NAME_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String name,
        @Schema(name = TransactionDtoConstants.EXPECTED_SUM,
                example = TransactionDtoConstants.EXPECTED_SUM_EXAMPLE,
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        @Digits(integer = 9, fraction = 2)
        BigDecimal expectedSum,
        @Schema(name = TransactionDtoConstants.ACHIEVED_BEFORE,
                example = TransactionDtoConstants.ACHIEVED_BEFORE_EXAMPLE,
                requiredMode = REQUIRED)
        @Date
        @DateAfterToday
        @NotBlank
        String achievedBefore,
        @Schema(name = TransactionDtoConstants.CURRENCY,
                example = TransactionDtoConstants.CURRENCY_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String currency){}
