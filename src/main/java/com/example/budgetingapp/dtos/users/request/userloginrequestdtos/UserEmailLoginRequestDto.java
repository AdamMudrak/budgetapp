package com.example.budgetingapp.dtos.users.request.userloginrequestdtos;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.UserDtoConstants;
import com.example.budgetingapp.validation.email.Email;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserEmailLoginRequestDto(
        @Schema(name = UserDtoConstants.EMAIL,
        example = UserDtoConstants.EMAIL_EXAMPLE,
        requiredMode = REQUIRED)
        @NotBlank
        @Email
        String email,

        @Schema(name = UserDtoConstants.PASSWORD,
        example = UserDtoConstants.PASSWORD_EXAMPLE,
        description = UserDtoConstants.PASSWORD_DESCRIPTION,
        requiredMode = REQUIRED)
        @Size(min = UserDtoConstants.MIN_PASSWORD_SIZE,
                max = UserDtoConstants.MAX_PASSWORD_SIZE)
        @NotBlank
        String password) {
}
