package com.example.budgetingapp.dtos.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramAuthenticationRequestDto(String firstName,
                                             String lastName,
                                             String phoneNumber,
                                             String password) {
}
