package com.example.budgetingapp.constants;

public class Constants {
    public static final String NO_ACCOUNT = "";

    public static final int MONTHS_IN_A_YEAR = 12;

    public static final String SPLITERATOR = "=";

    public static final String INVALID_ENTITY_VALUE =
            "One of the parameters is invalid. API will show which one";
    public static final String AUTHORIZATION_REQUIRED = "Authorization required";
    public static final String ACCESS_DENIED = "Access to this resource on the server is denied";

    public static final String CODE_200 = "200";
    public static final String CODE_201 = "201";
    public static final String CODE_204 = "204";
    public static final String CODE_400 = "400";
    public static final String CODE_401 = "401";
    public static final String CODE_403 = "403";

    public static final String ROLE_USER = "hasRole('ROLE_USER')";
    public static final String ROLE_ADMIN = "hasRole('ROLE_ADMIN')";

    public static final String CATEGORY_PAGEABLE_EXAMPLE = """
            {
              "page": 0,
              "size": 10,
              "sort":"name,DESC"
            }
            """;

    public static final String TRANSACTION_PAGEABLE_EXAMPLE = """
            {
              "page": 0,
              "size": 10,
              "sort":"transactionDate,ASC"
            }
            """;
}
