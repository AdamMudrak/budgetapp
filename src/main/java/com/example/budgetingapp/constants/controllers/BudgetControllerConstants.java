package com.example.budgetingapp.constants.controllers;

public class BudgetControllerConstants {
    public static final String BUDGET_API_NAME = "Budget API";
    public static final String BUDGET_API_DESCRIPTION = """
            Here you'll find a comprehensive overview
            of how to add, get and delete user's
            budgets for specific categories
            in this app.
            """;
    public static final String BUDGET = "/budgets";
    public static final String ADD_BUDGET = "/add-budget";
    public static final String GET_ALL_BUDGETS = "/get-all-budgets";
    public static final String DELETE_BUDGET_BY_ID = "/delete-budget/{budgetId}";

    public static final String ADD_BUDGET_SUMMARY = "Add a new user's budget";
    public static final String SUCCESSFULLY_ADDED = "Successfully added a budget";

    public static final String GET_ALL_BUDGETS_SUMMARY = "Get all user's budgets";
    public static final String SUCCESSFULLY_RETRIEVED = "Successfully retrieved budgets";

    public static final String DELETE_BUDGET_BY_ID_SUMMARY = "Delete a user's budget";
    public static final String SUCCESSFULLY_DELETED = "Successfully deleted a budget";
}
