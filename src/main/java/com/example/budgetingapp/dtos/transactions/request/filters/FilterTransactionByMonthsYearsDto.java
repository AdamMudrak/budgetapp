package com.example.budgetingapp.dtos.transactions.request.filters;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.validation.filtertype.ValidFilterType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FilterTransactionByMonthsYearsDto(
        @Schema(name = "accountId",
                example = "1",
                requiredMode = REQUIRED)
        @NotNull
        @Positive
        Long accountId,
        @Schema(name = "categoryIds",
                example = "[1, 2]")
        Set<Long> categoryIds,
        @Schema(name = "filterType",
                example = "MONTH/YEAR")
        @ValidFilterType
        String filterType) {
}
