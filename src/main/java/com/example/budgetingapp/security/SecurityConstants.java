package com.example.budgetingapp.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SecurityConstants {
    public static final String PATH = "http://localhost:8080/auth/password-reset?";
    public static final String RANDOM_STRING_BASE =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final String ACCESS = "ACCESS";
    public static final String RESET = "RESET";
    public static final String REFRESH = "REFRESH";
    public static final String INITIATE_RANDOM_PASSWORD_SUBJECT =
            "Initiate password reset for Moneta";
    public static final String RANDOM_PASSWORD_SUBJECT = "New password for Moneta";
    public static final String INITIATE_RANDOM_PASSWORD_BODY = """
            Good day! This email is to help you
            reset password for your moneta account.
            Please, use this link token to get a random password.""";
    public static final String RANDOM_PASSWORD_BODY = """
            Your new random password:
            """;
    public static final String SUCCESSFUL_RESET_MSG = """
            Your password has been reset successfully.
            Check email to see your new random password.
            We recommend setting a new meaningful password
            as soon as possible""";
}
