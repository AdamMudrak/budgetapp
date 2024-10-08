package com.example.budgetingapp.constants.security;

public class SecurityConstants {

    public static final int RANDOM_PASSWORD_STRENGTH = 25;
    public static final String RANDOM_STRING_BASE =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final String JWT_ACCESS_EXPIRATION = "${jwt.access.expiration}";
    public static final String JWT_RESET_EXPIRATION = "${jwt.reset.expiration}";
    public static final String JWT_REFRESH_EXPIRATION = "${jwt.refresh.expiration}";
    public static final String JWT_SECRET = "${jwt.secret}";
    public static final String EMAIL_SECRET = "email.secret";
    public static final int BEGIN_INDEX = 7;
    public static final String ACCESS = "ACCESS";
    public static final String RESET = "RESET";
    public static final String REFRESH = "REFRESH";

    public static final String INITIATE_RANDOM_PASSWORD_SUBJECT =
            "Initiate password reset for Moneta";
    public static final String RANDOM_PASSWORD_SUBJECT = "New password for Moneta";
    public static final String INITIATE_RANDOM_PASSWORD_BODY = """
            Good day! This email is to help you
            reset password for your moneta account.
            Please, use this link to get a random password.""";
    public static final String RANDOM_PASSWORD_BODY = """
            Your new random password:
            """;
    public static final String SUCCESSFUL_RESET_MSG = """
            Your password has been reset successfully.
            Check email to see your new random password.
            We recommend setting a new meaningful password
            as soon as possible""";
    public static final String SUCCESSFUL_CHANGE_MESSAGE =
            "New password has been set successfully";

    public static final String SUCCESS_EMAIL = "An email with reset link has been sent";
}
