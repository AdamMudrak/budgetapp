package com.example.budgetingapp.constants.controllers;

public class TargetControllerConstants {
    public static final String TARGET_API_NAME = "Target API";
    public static final String TARGET_API_DESCRIPTION = """
            Here you'll find a comprehensive overview
            of how to add target, replenish it and "break" it,
            retrieving all the money to one of the
            accounts in this app.
            """;

    public static final String TARGETS = "/targets";

    public static final String ADD_TARGET = "/add-target";
    public static final String ADD_TARGET_SUMMARY = "Add target.";
    public static final String SUCCESSFULLY_ADDED_TARGET =
            "Successfully added a target.";

    public static final String REPLENISH_TARGET = "/replenish-target";
    public static final String REPLENISH_SUMMARY = "Replenish target.";
    public static final String SUCCESSFULLY_REPLENISHED_TARGET =
            "Successfully replenished a target.";

    public static final String GET_ALL_TARGETS = "/get-all-targets";
    public static final String GET_ALL_TARGETS_SUMMARY = "Retrieve all targets.";
    public static final String SUCCESSFULLY_RETRIEVED_TARGETS =
            "Successfully retrieved all targets.";

    public static final String DESTROY_TARGET = "/destroy-target";
    public static final String DESTROY_TARGET_SUMMARY =
            "Destroy a target, retrieve money to one of your accounts.";
    public static final String SUCCESSFULLY_DESTROYED_TARGET =
            "Successfully destroy a target and automatically replenish the account chosen.";
}
