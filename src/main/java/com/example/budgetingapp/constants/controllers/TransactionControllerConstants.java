package com.example.budgetingapp.constants.controllers;

public class TransactionControllerConstants {
    public static final String TRANSACTION_API_NAME = "Expense & Income Transactions API";
    public static final String TRANSACTION_API_DESCRIPTION = """
            Here you'll find a comprehensive overview
            of how to work with transactions in this app
            """;
    public static final String EXPENSE_TRANSACTIONS = "/expense-transactions";
    public static final String INCOME_TRANSACTIONS = "/income-transactions";

    public static final String EXPENSE = "EXPENSE";
    public static final String ADD_EXPENSE = "/add-expense";
    public static final String GET_ALL_EXPENSES = "/get-all-expenses";
    public static final String GET_ALL_ACCOUNT_EXPENSES = "/get-all-expenses/{accountId}";
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

    public static final String GET_ALL_EXPENSES_SUMMARY = "Retrieve all expenses";
    public static final String SUCCESSFULLY_RETRIEVED_EXPENSES =
            "Successfully retrieved expenses";

    public static final String GET_ALL_ACCOUNT_EXPENSES_SUMMARY = "Retrieve all account expenses";
    public static final String SUCCESSFULLY_RETRIEVED_ACCOUNT_EXPENSES =
            "Successfully retrieved account expenses";

    public static final String INCOME = "INCOME";
    public static final String ADD_INCOME = "/add-income";
    public static final String GET_ALL_INCOMES = "/get-all-incomes";
    public static final String GET_ALL_ACCOUNT_INCOMES = "/get-all-incomes/{accountId}";
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

    public static final String GET_ALL_INCOMES_SUMMARY = "Retrieve all incomes";
    public static final String SUCCESSFULLY_RETRIEVED_INCOMES =
            "Successfully retrieved incomes";

    public static final String GET_ALL_ACCOUNT_INCOMES_SUMMARY = "Retrieve all account incomes";
    public static final String SUCCESSFULLY_RETRIEVED_ACCOUNT_INCOMES =
            "Successfully retrieved account incomes";

}
