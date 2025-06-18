package com.example.budgetingapp.exceptions;

public class ActionNotFoundException extends RuntimeException {
    public ActionNotFoundException(String message) {
        super(message);
    }
}
