package com.example.budgetingapp.constants.controllers;

public class AuthControllerConstants {
    public static final String AUTH_API_NAME = "Authentication API";
    public static final String AUTH_API_DESCRIPTION = """
            Here you'll find a comprehensive overview
            of how to register and login in this app.
            Also, you can use this API to reset password,
            get a new random password instead, and set
            a new password afterwards
            """;

    public static final String REGISTER_SUMMARY = "Register a new user in the app";
    public static final String SUCCESSFULLY_REGISTERED = "Successfully registered a new user";

    public static final String CONFIRM_SUMMARY = "Confirm registration of a new user in the app";
    public static final String SUCCESSFULLY_CONFIRMED = "Successfully confirmed registration";

    public static final String TELEGRAM_LOGIN_SUMMARY = "Log in using existing telegram account";
    public static final String SUCCESSFULLY_LOGGED_IN = "Successfully logged in in the app";

    public static final String EMAIL_LOGIN_SUMMARY = "Log in using existing email account";

    public static final String INITIATE_PASSWORD_RESET_SUMMARY =
            "Initiate password reset via a link sent to your email";
    public static final String SUCCESSFULLY_INITIATED_PASSWORD_RESET =
            "Successfully initiated password reset";

    public static final String RESET_PASSWORD_SUMMARY = "Reset password using the link sent";
    public static final String SUCCESSFULLY_RESET_PASSWORD = "Successfully reset password";

    public static final String CHANGE_PASSWORD_SUMMARY =
            "Change password while being logged in, either using a random or your own password";
    public static final String SUCCESSFULLY_CHANGE_PASSWORD = "Successfully changed password";

    public static final String TELEGRAM_AUTH_SUMMARY =
            "Register or login using telegram";
    public static final String SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM =
            "Successfully authenticated using telegram";

    public static final String REFRESH_ACCESS_TOKEN_SUMMARY =
            "Using a Refresh Token, get a new Access Token";
    public static final String SUCCESSFULLY_REFRESHED_TOKEN =
            "Successfully refreshed access token using refresh token";
}
