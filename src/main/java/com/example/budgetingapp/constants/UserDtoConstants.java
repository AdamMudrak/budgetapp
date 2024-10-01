package com.example.budgetingapp.constants;

public class UserDtoConstants {
    public static final String EMAIL = "email";
    public static final String EMAIL_EXAMPLE = "example@gmail.com";

    public static final String PASSWORD = "password";
    public static final String PASSWORD_EXAMPLE = "Best_Password1@3$";
    public static final String PASSWORD_DESCRIPTION = """
                    Your password should contain:
                    1) at least one lowercase letter, like 'a';
                    2) at least one uppercase letter, like 'A';
                    3) at least one number, like '0';
                    4) at least one special character, like '?!@#$%^&*~';
                    5) from 8 to 32 characters.""";

    public static final String REPEAT_PASSWORD = "repeatPassword";
    public static final String REPEAT_PASSWORD_EXAMPLE = PASSWORD_EXAMPLE;
    public static final String REPEAT_PASSWORD_DESCRIPTION =
            "This field must be the same as password!";

    public static final String FIRST_NAME = "firstName";
    public static final String FIRST_NAME_EXAMPLE = "John";

    public static final String LAST_NAME = "lastName";
    public static final String LAST_NAME_EXAMPLE = "Wick";

    public static final String SHIPPING_ADDRESS = "shippingAddress";
    public static final String SHIPPING_ADDRESS_EXAMPLE =
            "132, My Street, Kingston, New York 12401";
}
