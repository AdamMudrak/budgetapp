package com.example.budgetingapp.dtos.users.request.userlogindtos;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.UserDtoConstants;
import com.example.budgetingapp.validation.phone.Phone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserTelegramLoginDto(
        @Schema(name = UserDtoConstants.TELEGRAM_PHONE_NUMBER,
        example = UserDtoConstants.TELEGRAM_PHONE_NUMBER_EXAMPLE,
        requiredMode = REQUIRED)
        @NotBlank
        @Phone
        String phoneNumber,

        @Schema(name = UserDtoConstants.PASSWORD,
        example = UserDtoConstants.PASSWORD_EXAMPLE,
        description = UserDtoConstants.PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @NotBlank
        String password) {
}
