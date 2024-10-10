package com.example.budgetingapp.security.services;

import com.example.budgetingapp.dtos.user.request.UserLoginRequestDto;
import com.example.budgetingapp.dtos.user.request.UserSetNewPasswordRequestDto;
import com.example.budgetingapp.dtos.user.response.UserLoginResponseDto;
import com.example.budgetingapp.dtos.user.response.UserPasswordResetResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto requestDto);

    UserPasswordResetResponseDto initiatePasswordReset(String email);

    UserPasswordResetResponseDto confirmResetPassword(String token);

    UserPasswordResetResponseDto changePassword(String token,
                                        UserSetNewPasswordRequestDto userSetNewPasswordRequestDto);
}
