package com.example.budgetingapp.controllers;

import com.example.budgetingapp.constants.AuthConstants;
import com.example.budgetingapp.constants.Constants;
import com.example.budgetingapp.dtos.user.request.UserLoginRequestDto;
import com.example.budgetingapp.dtos.user.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.user.response.UserLoginResponseDto;
import com.example.budgetingapp.dtos.user.response.UserRegistrationResponseDto;
import com.example.budgetingapp.exceptions.RegistrationException;
import com.example.budgetingapp.security.AuthenticationService;
import com.example.budgetingapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = AuthConstants.AUTH_API_NAME,
        description = AuthConstants.AUTH_API_DESCRIPTION)
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = AuthConstants.REGISTER_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description = Constants.SUCCESSFULLY_REGISTERED)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @PostMapping("/registration")
    public UserRegistrationResponseDto registerUser(
            @RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = AuthConstants.LOGIN_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description = Constants.SUCCESSFULLY_LOGGED_IN)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    //TODO delete later
    //For testing purposes
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponse(responseCode = Constants.CODE_200, description = Constants.SUCCESSFULLY_RETRIEVED)
    @GetMapping
    public String getAnyMessage() {
        return "Anybody can see this message";
    }

    //TODO delete later
    //For testing purposes
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = Constants.CODE_200, description = Constants.SUCCESSFULLY_RETRIEVED)
    @GetMapping("/admin")
    public String getAdminMessage() {
        return "Only admin can see this message";
    }
}
