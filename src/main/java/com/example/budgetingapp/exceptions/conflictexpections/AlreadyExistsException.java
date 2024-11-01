package com.example.budgetingapp.exceptions.conflictexpections;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
