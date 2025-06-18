package com.example.budgetingapp.exceptions;

public class PasswordMismatch extends RuntimeException {
    public PasswordMismatch(String message) {
        super(message);
    }
}
