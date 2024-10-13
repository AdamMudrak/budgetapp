package com.example.budgetingapp.security.services;

import com.example.budgetingapp.dtos.user.request.TelegramAuthenticationRequestDto;
import com.example.budgetingapp.dtos.user.response.TelegramAuthenticationResponseDto;

public interface TelegramAuthenticationService {
    TelegramAuthenticationResponseDto register(TelegramAuthenticationRequestDto requestDto);
}
