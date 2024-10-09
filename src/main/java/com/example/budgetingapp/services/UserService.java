package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.user.request.UserRegistrationRequestDto;
import com.example.budgetingapp.exceptions.RegistrationException;

public interface UserService {
    String register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    String confirmRegistration(String email);
}
