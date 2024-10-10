package com.example.budgetingapp.services;

public interface MessageService {
    void sendMessage(String toWho, String setText);

    void sendActionMessage(String text, String action);

    void sendResetPassword(String toWho, String randomPassword);
}
