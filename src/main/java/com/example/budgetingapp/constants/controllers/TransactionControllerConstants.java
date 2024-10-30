package com.example.budgetingapp.constants.controllers;

public class TransactionControllerConstants {
    public static final String TRANSACTION_API_NAME = "Expense & Income Transactions API";
    public static final String TRANSACTION_API_DESCRIPTION = """
            Here you'll find a comprehensive overview
            of how to work with transactions in this app
            """;
    public static final String EXPENSE_TRANSACTIONS = "/expense-transactions";
    public static final String INCOME_TRANSACTIONS = "/income-transactions";
    public static final String TRANSFERS = "/transfers";

    public static final String ADD_TRANSFER = "/add-transfer";
    public static final String ADD_TRANSFER_SUMMARY = "Add transfer";
    public static final String SUCCESSFULLY_ADDED_TRANSFER =
            "Successfully added a transfer transaction";

    public static final String GET_ALL_TRANSFERS = "/get-all-transfers";
    public static final String GET_ALL_TRANSFERS_SUMMARY = "Retrieve all transfers";
    public static final String SUCCESSFULLY_RETRIEVED_TRANSFERS =
            "Successfully retrieved transfer transactions";

    public static final String EXPENSE = "EXPENSE";
    public static final String ADD_EXPENSE = "/add-expense";
    public static final String GET_ALL_EXPENSES = "/get-all-expenses";
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

    public static final String INCOME = "INCOME";
    public static final String ADD_INCOME = "/add-income";
    public static final String GET_ALL_INCOMES = "/get-all-incomes";
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

}
