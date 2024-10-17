package com.example.budgetingapp.dtos.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramAuthenticationRequestDto(String userName,
                                             String password,
                                             String token) {
}
