package com.example.budgetingapp.dtos.transactions.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateCategoryDto(String newName) {
}
