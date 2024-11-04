package com.example.budgetingapp.dtos.users.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramAuthenticationRequestDto(
        @NotNull
        String userName,
        @NotNull
        String password,
        @NotNull
        String token) {
}
