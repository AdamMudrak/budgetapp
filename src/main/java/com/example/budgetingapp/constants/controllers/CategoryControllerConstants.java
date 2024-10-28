package com.example.budgetingapp.constants.controllers;

public class CategoryControllerConstants {
    public static final String INCOME_CATEGORY_API_NAME = "Income Category API";
    public static final String EXPENSE_CATEGORY_API_NAME = "Expense Category API";
    public static final String CATEGORY_API_DESCRIPTION = """
            Here you'll find a comprehensive overview
            of how to work with categories of transactions
            in this app""";
    public static final String INCOME_CATEGORIES = "/income-categories";
    public static final String EXPENSE_CATEGORIES = "/expense-categories";

    public static final String ADD_CATEGORY = "/add-category";
    public static final String GET_ALL_CATEGORIES = "/get-all-categories";
    public static final String UPDATE_CATEGORY_BY_ID = "/update-category/{categoryId}";
    public static final String DELETE_CATEGORY_BY_ID = "/delete-category/{categoryId}";

    public static final String ADD_CATEGORY_SUMMARY = "Add category";
    public static final String SUCCESSFULLY_ADDED_CATEGORY =
            "Successfully added a category";

    public static final String UPDATE_CATEGORY_SUMMARY = "Update category";
    public static final String SUCCESSFULLY_UPDATE_CATEGORY =
            "Successfully updated a category";

    public static final String DELETE_CATEGORY_SUMMARY = "Delete category";
    public static final String SUCCESSFULLY_DELETE_CATEGORY =
            "Successfully deleted a category";

    public static final String GET_ALL_CATEGORIES_SUMMARY = "Get all user's categories";
    public static final String SUCCESSFULLY_RETRIEVED_CATEGORIES =
            "Successfully retrieved categories";
}
