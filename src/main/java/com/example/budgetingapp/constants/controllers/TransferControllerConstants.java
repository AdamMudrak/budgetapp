package com.example.budgetingapp.constants.controllers;

public class TransferControllerConstants {
    public static final String TRANSFERS_API_NAME = "Transfers API";
    public static final String TRANSFERS_API_DESCRIPTION = """
            Here you'll find a comprehensive overview
            of how to transfer money from one account to another,
            undo this transfer, overview all transfers
            """;
    public static final String TRANSFERS = "/transfers";

    public static final String ADD_TRANSFER = "/add-transfer";
    public static final String ADD_TRANSFER_SUMMARY = "Add transfer";
    public static final String SUCCESSFULLY_ADDED_TRANSFER =
            "Successfully added a transfer transaction";

    public static final String GET_ALL_TRANSFERS = "/get-all-transfers";
    public static final String GET_ALL_TRANSFERS_SUMMARY = "Retrieve all transfers";
    public static final String SUCCESSFULLY_RETRIEVED_TRANSFERS =
            "Successfully retrieved transfer transactions";

    public static final String DELETE_TRANSFER_BY_ID = "/delete-transfer/{transferId}";
    public static final String DELETE_TRANSFER_BY_ID_SUMMARY = "Delete a transfer by its id";
    public static final String SUCCESSFULLY_DELETED_TRANSFER =
            "Successfully deleted a transfer transaction";
}
