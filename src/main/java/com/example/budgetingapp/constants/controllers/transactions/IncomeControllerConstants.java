package com.example.budgetingapp.constants.controllers.transactions;

public class IncomeControllerConstants {
    public static final String INCOME_TRANSACTIONS = "/income-transactions";
    public static final String INCOME = "INCOME";
    public static final String ADD_INCOME = "/add-income";
    public static final String GET_ALL_INCOMES = "/get-all-incomes";
    public static final String GET_ALL_INCOMES_FOR_CHARTS_DAYS =
            "/get-all-incomes-for-charts-days";
    public static final String GET_ALL_INCOMES_FOR_CHARTS_MONTHS_YEARS =
            "/get-all-incomes-for-charts-months-years";
    public static final String UPDATE_INCOME_BY_ID = "/update-income/{transactionId}";
    public static final String DELETE_INCOME_BY_ID = "/delete-income/{transactionId}";

    public static final String ADD_INCOME_SUMMARY = "Add income";
    public static final String SUCCESSFULLY_ADDED_INCOME =
            "Successfully added an income transaction";

    public static final String UPDATE_INCOME_SUMMARY = "Update income";
    public static final String SUCCESSFULLY_UPDATED_INCOME =
            "Successfully updated an income transaction";

    public static final String DELETE_INCOME_SUMMARY = "Delete income";
    public static final String SUCCESSFULLY_DELETED_INCOME =
            "Successfully deleted an income transaction";

    public static final String GET_ALL_INCOMES_SUMMARY = "Retrieve all incomes, or optionally"
            + " filter them by: accountId, fromDate, toDate, categoryIds";
    public static final String SUCCESSFULLY_RETRIEVED_INCOMES =
            "Successfully retrieved incomes";

    public static final String GET_ALL_INCOMES_FOR_CHARTS_SUMMARY =
            "Retrieve all incomes grouped by month or year, "
                    + "then grouped by categories";
    public static final String SUCCESSFULLY_RETRIEVED_INCOMES_FOR_CHARTS =
            "Successfully retrieved incomes for charts";

    public static final String GET_ALL_INCOMES_FOR_CHARTS_SUMMARY_DAY =
            "Retrieve all incomes grouped by day, "
                    + "then grouped by categories";
    public static final String SUCCESSFULLY_RETRIEVED_INCOMES_FOR_CHARTS_DAY =
            "Successfully retrieved incomes for day charts";
}
