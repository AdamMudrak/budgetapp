package com.example.budgetingapp.dtos.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//TODO VALIDATE

@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramAuthenticationRequestDto(String firstName,
                                             String lastName,
                                             String email,
                                             String password,
                                             String token) {
}
