package com.example.budgetingapp.dtos.support.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.ContactUsDtoConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SupportRequestDto(
        @Schema(name = ContactUsDtoConstants.NAME,
                example = ContactUsDtoConstants.NAME_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String name,
        @Schema(name = ContactUsDtoConstants.EMAIL,
                example = ContactUsDtoConstants.EMAIL_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        @Email
        String email,
        @Schema(name = ContactUsDtoConstants.MESSAGE,
                example = ContactUsDtoConstants.MESSAGE_EXAMPLE,
                requiredMode = REQUIRED)
        @NotBlank
        String message) {
}
