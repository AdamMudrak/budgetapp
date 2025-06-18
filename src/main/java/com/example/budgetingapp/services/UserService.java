package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.users.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.users.response.UserRegistrationResponseDto;
import com.example.budgetingapp.exceptions.RegistrationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    ResponseEntity<Void> confirmRegistration(HttpServletRequest httpServletRequest);
}
