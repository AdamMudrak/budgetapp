package com.example.budgetingapp.constants.validation;

public class ValidationConstants {
    public static final String INVALID_DATE_FORMAT = "Invalid date format. Should be YYYY-MM-dd";
    public static final String INVALID_FILTER = "Invalid filter. Must be MONTH or YEAR";
    public static final String DATE_BEFORE_TODAY = "Invalid date. Can't be earlier than today";
    public static final String TO_DATE_EARLIER_THAN_FROM_DATE = "Invalid date. "
            + "to-date can't be earlier than from-date";
    public static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    public static final String INVALID_EMAIL = "Invalid email";
    public static final String PATTERN_OF_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*"
            + "@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static final String PASSWORD_COLLISION =
            "currentPassword collides with newPassword. Try again";
    public static final String PASSWORD_MISMATCH =
            "password and repeatPassword don't match. Try again";
    public static final String NEW_PASSWORD_MISMATCH =
            "newPassword and repeatPassword don't match. Try again";

    public static final String INVALID_PASSWORD =
            "Password should contain"
                    + " 1 lowercase letter,"
                    + " 1 uppercase letter,"
                    + " 1 digit,"
                    + " 1 special character,"
                    + " and be 8 to 32 characters long";
    public static final String PASSWORD_PATTERN = "(?=^.*[A-Z])(?=^.*[a-z])(?=^.*\\d)"
            + "(?=^.*[?!@#$%^&*~]).{8,32}$";
}
