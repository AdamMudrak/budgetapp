package com.example.budgetingapp.constants.controllers.transactions;

public class ExpenseControllerConstants {
    public static final String EXPENSE_TRANSACTIONS = "/expense-transactions";
    public static final String EXPENSE = "EXPENSE";
    public static final String ADD_EXPENSE = "/add-expense";
    public static final String GET_ALL_EXPENSES = "/get-all-expenses";
    public static final String GET_ALL_EXPENSES_FOR_CHARTS_DAYS =
            "/get-all-expenses-for-charts-days";
    public static final String GET_ALL_EXPENSES_FOR_CHARTS_MONTHS_YEARS =
            "/get-all-expenses-for-charts-months-years";

    public static final String UPDATE_EXPENSE_BY_ID = "/update-expense/{transactionId}";
    public static final String DELETE_EXPENSE_BY_ID = "/delete-expense/{transactionId}";

    public static final String ADD_EXPENSE_SUMMARY = "Add expense";
    public static final String SUCCESSFULLY_ADDED_EXPENSE =
            "Successfully added an expense transaction";

    public static final String UPDATE_EXPENSE_SUMMARY = "Update expense";
    public static final String SUCCESSFULLY_UPDATED_EXPENSE =
            "Successfully updated an expense transaction";

    public static final String DELETE_EXPENSE_SUMMARY = "Delete expense";
    public static final String SUCCESSFULLY_DELETED_EXPENSE =
            "Successfully deleted an expense transaction";

    public static final String GET_ALL_EXPENSES_SUMMARY = "Retrieve all expenses, or optionally"
            + " filter them by: accountId, fromDate, toDate, categoryIds";
    public static final String SUCCESSFULLY_RETRIEVED_EXPENSES =
            "Successfully retrieved expenses";

    public static final String GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY =
            "Retrieve all expenses grouped by month or year, "
                    + "then grouped by categories";
    public static final String SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS =
            "Successfully retrieved expenses for charts";

    public static final String GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY_DAY =
            "Retrieve all expenses grouped by day, "
                    + "then grouped by categories";
    public static final String SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS_DAY =
            "Successfully retrieved expenses for day charts";
}
