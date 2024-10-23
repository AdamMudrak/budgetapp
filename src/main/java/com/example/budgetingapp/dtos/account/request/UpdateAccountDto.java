package com.example.budgetingapp.dtos.account.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateAccountDto(
        @NotBlank
        String currentName,
        @NotBlank
        String newName){}
