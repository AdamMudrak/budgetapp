package com.example.budgetingapp.exceptions;

public class ActionNotFoundException extends RuntimeException {
    ActionNotFoundException(String message) {
        super(message);
    }
}
