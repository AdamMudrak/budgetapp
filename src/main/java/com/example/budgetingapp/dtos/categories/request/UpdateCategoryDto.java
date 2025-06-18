package com.example.budgetingapp.dtos.categories.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateCategoryDto(
        @Schema(name = "newName",
                example = "Petrol cards",
                requiredMode = REQUIRED)
        @NotBlank
        String newName) {
}
