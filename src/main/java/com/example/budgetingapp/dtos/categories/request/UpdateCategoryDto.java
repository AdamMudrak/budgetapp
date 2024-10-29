package com.example.budgetingapp.dtos.categories.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.CategoriesConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateCategoryDto(
        @Schema(name = CategoriesConstants.NEW_NAME,
                example = CategoriesConstants.NAME_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String newName) {
}
