package com.example.budgetingapp.dtos.accounts.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateAccountDto(
        @Schema(name = "newName",
                example = "another credit card",
                requiredMode = REQUIRED)
        @NotBlank
        String newName){}
