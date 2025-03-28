package com.example.budgetingapp.services.interfaces;

import com.example.budgetingapp.dtos.users.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.users.response.UserRegistrationResponseDto;
import com.example.budgetingapp.exceptions.badrequest.RegistrationException;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    ResponseEntity<Void> confirmRegistration(String email);
}
