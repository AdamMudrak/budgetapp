package com.example.budgetingapp.constants.security;

public class SecurityConstants {
    public static final String PLUS = "+";
    public static final int RANDOM_ACTION_JWT_STRENGTH = 24;
    public static final int RANDOM_PASSWORD_STRENGTH = 32;
    public static final int RANDOM_LINK_STRENGTH = 128;
    public static final String RANDOM_STRING_BASE =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final String JWT_ACCESS_EXPIRATION = "${jwt.access.expiration}";
    public static final String JWT_ACTION_EXPIRATION = "${jwt.action.expiration}";
    public static final String JWT_REFRESH_EXPIRATION = "${jwt.refresh.expiration}";
    public static final String JWT_SECRET = "${jwt.secret}";
    public static final String SERVER_PATH = "${server.path}";
    public static final String RESET_PATH = "${reset.path}";
    public static final String CONFIRMATION_PATH = "${confirmation.path}";
    public static final int BEGIN_INDEX = 7;
    public static final String RESET = "RESET";
    public static final String CONFIRMATION = "CONFIRMATION";
    public static final String ACCESS = "ACCESS";
    public static final String ACTION = "ACTION";
    public static final String REFRESH = "REFRESH";

    public static final String CONFIRM_REGISTRATION_SUBJECT =
            "Finish registration in Moneta";
    public static final String CONFIRM_REGISTRATION_BODY = """
            Good day! This email is to help you
            confirm you registration of moneta account.
            Please, use this link finish it.""";
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
    public static final String REGISTERED_BUT_NOT_ACTIVATED =
            "User is registered but not enabled. "
                    + "Check your email to confirm registration. "
                    + "Your account will not be available until then";
    public static final String REGISTERED =
            "User is registered successfully. "
                    + "Check your email to confirm registration. "
                    + "Your account will not be enabled until then";
    public static final String REGISTERED_AND_CONFIRMED =
            "Your registration is successfully confirmed";
    public static final String SUCCESS_EMAIL = "An email with reset link has been sent";

    public static final String TOKEN = "${telegram.bot.token}";
    public static final String BOT_TO_SERVER_URI = "${bot.to.server.uri}";
    public static final String BOT_NAME = "BudgetApplicationBot";
    public static final String START = "/start";
    public static final String STOP = "/stop";
    public static final String STOPPED_SUCCESS = "The bot has been stopped!";
    public static final String UNKNOWN_COMMAND = "Unknown command. Please use /start, or /stop.";
    public static final String TELEGRAM_REGISTRATION =
            "To register in our app, you'll need"
                    + System.lineSeparator()
                    + "to share your data with us, specifically:"
                    + System.lineSeparator()
                    + "your phone number, name, last name"
                    + System.lineSeparator()
                    + "This data is going to be used only in"
                    + System.lineSeparator()
                    + "registration purposes. You'll then receive"
                    + System.lineSeparator()
                    + "a password to login in the app";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CONTENT_TYPE = "application/json";
    public static final String FAILED = "Something went wrong... Please try again later";
}
