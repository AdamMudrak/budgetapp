package com.example.budgetingapp.dtos.user.request;

public record UserSetNewPasswordRequestDto(String currentPassword,
                                           String newPassword,
                                           String repeatNewPassword) {}
