package com.example.budgetingapp.constants.controllers;

public class AccountControllerConstants {
    public static final String ACCOUNT_API_NAME = "Account API";
    public static final String ACCOUNT_API_DESCRIPTION = """
            Here you'll find a comprehensive overview
            of how to add, get and update a user's money
            account in this app.
            """;
    public static final String ACCOUNT = "/account";
    public static final String ADD_ACCOUNT = "/add-account";
    public static final String GET_ALL_ACCOUNTS = "/get-all-accounts";
    public static final String UPDATE_ACCOUNT = "/update-account/{accountId}";
    public static final String SET_ACCOUNT_BY_DEFAULT = "/set-account-by-default/{accountId}";
    public static final String GET_ACCOUNT_BY_DEFAULT = "/get-account-by-default";
    public static final String GET_ACCOUNT_BY_ID = "/get-account-by-id/{accountId}";

    public static final String ADD_ACCOUNT_SUMMARY = "Add a new user's account";
    public static final String SUCCESSFULLY_ADDED = "Successfully added an account";

    public static final String GET_ALL_ACCOUNTS_SUMMARY = "Get all user's account";
    public static final String SUCCESSFULLY_RETRIEVED = "Successfully retrieved an account";

    public static final String UPDATE_ACCOUNT_SUMMARY = "Update a user's account name";
    public static final String SUCCESSFULLY_UPDATED = "Successfully updated an account";

    public static final String SET_ACCOUNT_BY_DEFAULT_SUMMARY = "Set a new account by default";
    public static final String SUCCESSFULLY_SET = "Successfully set a new account by default";

    public static final String GET_ACCOUNT_BY_DEFAULT_SUMMARY = "Get the account by default";
    public static final String SUCCESSFULLY_RETRIEVED_DEFAULT_ACCOUNT =
            "Successfully retrieved the account by default";

    public static final String GET_ACCOUNT_BY_ID_SUMMARY = "Get user's account by id";
    public static final String SUCCESSFULLY_RETRIEVED_ACCOUNT_BY_ID =
            "Successfully retrieved account by id";
}
