package com.example.budgetingapp.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SecurityConstants {
    public static final String ACCESS = "ACCESS";
    public static final String RESET = "RESET";
    public static final String REFRESH = "REFRESH";
    public static final String RESET_PASSWORD_SUBJECT = "Reset password  for Moneta";
    public static final String INITIATE_RANDOM_PASSWORD_BODY = """
            Good day! This email is to help you
            reset password for your moneta account.
            Please, use this link token to get a random password.""";
    public static final String RANDOM_PASSWORD_BODY = """
            Your new random password:
            """;
}
