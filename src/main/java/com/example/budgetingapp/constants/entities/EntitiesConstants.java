package com.example.budgetingapp.constants.entities;

import java.util.List;

public class EntitiesConstants {
    public static final int ACCOUNT_QUANTITY_THRESHOLD = 5;
    public static final int TARGET_QUANTITY_THRESHOLD = 10;
    public static final int BUDGET_QUANTITY_THRESHOLD = 15;
    public static final int CATEGORY_QUANTITY_THRESHOLD = 11;

    public static final String DEFAULT_ACCOUNT_NAME = "Default account";
    public static final String DEFAULT_ACCOUNT_CURRENCY = "USD";

    public static final List<String> DEFAULT_INCOME_CATEGORIES_LIST = List.of(
            "Salary",
            "Freelance",
            "Investments",
            "Rental Income",
            "Other",
            "Target Deletion");

    public static final List<String> DEFAULT_EXPENSE_CATEGORIES_LIST = List.of(
            "Groceries",
            "Utilities",
            "Transportation",
            "Entertainment",
            "Other",
            "Target Replenishment");

    public static final String DEFAULT_CATEGORY_NAME = "Other";
    public static final String TARGET_EXPENSE_CATEGORY = "Target Replenishment";
    public static final String TARGET_INCOME_CATEGORY = "Target Deletion";
}
