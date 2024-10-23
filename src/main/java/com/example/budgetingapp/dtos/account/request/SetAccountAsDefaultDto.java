package com.example.budgetingapp.dtos.account.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.AccountDtoConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SetAccountAsDefaultDto(
        @Schema(name = AccountDtoConstants.NAME,
                example = AccountDtoConstants.NAME_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String name) {
}
