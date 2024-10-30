package com.example.budgetingapp.constants.entities;

import java.util.List;

public class EntitiesConstants {
    public static final int ACCOUNT_QUANTITY_THRESHOLD = 5;
    public static final int CATEGORY_QUANTITY_THRESHOLD = 20;

    public static final String TRANSFERS = "transfers";
    public static final String FROM_ACCOUNT_ID = "from_account_id";
    public static final String TO_ACCOUNT_ID = "to_account_id";

    public static final String EXPENSES = "expenses";
    public static final String ACCOUNT_ID = "account_id";
    public static final String EXPENSE_CATEGORY_ID = "expense_category_id";
    public static final String EXPENSE_CATEGORIES = "expense_categories";

    public static final String INCOMES = "incomes";
    public static final String INCOME_CATEGORY_ID = "income_category_id";
    public static final String INCOME_CATEGORIES = "income_categories";

    public static final String ACCOUNTS = "accounts";
    public static final String USER_ID = "user_id";

    public static final String ACTION_TOKENS = "action_tokens";

    public static final String PARAM_TOKENS = "param_tokens";

    public static final String ROLES = "roles";

    public static final String USERS = "users";
    public static final String USERS_ROLES_JOIN_TABLE = "users_roles";
    public static final String ROLE_ID = "role_id";
    public static final String BOOLEAN_TO_INT = "TINYINT(1)";

    public static final String DEFAULT_ACCOUNT_NAME = "Default account";
    public static final String DEFAULT_ACCOUNT_CURRENCY = "USD";

    public static final List<String> DEFAULT_INCOME_CATEGORIES_LIST = List.of(
            "Salary",
            "Freelance",
            "Investments",
            "Rental Income",
            "Gifts");

    public static final List<String> DEFAULT_EXPENSE_CATEGORIES_LIST = List.of(
            "Groceries",
            "Utilities",
            "Transportation",
            "Entertainment",
            "Healthcare");
}
