package com.example.budgetingapp.dtos.account.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.AccountDtoConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateAccountDto(
        @Schema(name = AccountDtoConstants.NEW_NAME,
                example = AccountDtoConstants.NEW_NAME_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String newName){}
