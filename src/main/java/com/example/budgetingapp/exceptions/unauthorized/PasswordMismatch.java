package com.example.budgetingapp.exceptions.unauthorized;

public class PasswordMismatch extends RuntimeException {
    public PasswordMismatch(String message) {
        super(message);
    }
}
