package com.example.budgetingapp.exceptions.forbidden;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}
