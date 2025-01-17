package com.example.budgetingapp.exceptions.conflictexpections;

public class PasswordMismatch extends RuntimeException {
    public PasswordMismatch(String message) {
        super(message);
    }
}
