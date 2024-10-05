package com.example.budgetingapp.dtos.user.request;

public record UserResetRequestDto(String email, String token,
                                  String newPassword, String repeatNewPassword) {}
