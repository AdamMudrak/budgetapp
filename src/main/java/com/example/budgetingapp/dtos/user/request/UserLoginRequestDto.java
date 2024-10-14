package com.example.budgetingapp.dtos.user.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.UserDtoConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserLoginRequestDto(
        @Schema(name = UserDtoConstants.USER_NAME,
        example = UserDtoConstants.USER_NAME_EXAMPLE,
        requiredMode = REQUIRED)
        @NotBlank
        String userName,

        @Schema(name = UserDtoConstants.PASSWORD,
        example = UserDtoConstants.PASSWORD_EXAMPLE,
        description = UserDtoConstants.PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @Size(min = UserDtoConstants.MIN_PASSWORD_SIZE,
                max = UserDtoConstants.MAX_PASSWORD_SIZE)
        @NotBlank
        String password) {
}
