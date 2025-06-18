package com.example.budgetingapp.dtos.users.request.userlogindtos;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.UserDtoConstants;
import com.example.budgetingapp.validation.phone.Phone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserTelegramLoginDto(
        @Schema(name = "phoneNumber",
        example = "+380630000000",
        requiredMode = REQUIRED)
        @NotBlank
        @Phone
        String phoneNumber,

        @Schema(name = "password",
        example = "Best_Password1@3$",
        description = UserDtoConstants.PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @NotBlank
        String password) {
}
