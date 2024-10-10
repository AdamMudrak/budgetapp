package com.example.budgetingapp.services.impl;

import static com.example.budgetingapp.constants.Constants.SPLITERATOR;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRMATION;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRMATION_PATH;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRM_REGISTRATION_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRM_REGISTRATION_SUBJECT;
import static com.example.budgetingapp.constants.security.SecurityConstants.INITIATE_RANDOM_PASSWORD_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.INITIATE_RANDOM_PASSWORD_SUBJECT;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_SUBJECT;
import static com.example.budgetingapp.constants.security.SecurityConstants.RESET;
import static com.example.budgetingapp.constants.security.SecurityConstants.RESET_PATH;

import com.example.budgetingapp.exceptions.ActionNotFoundException;
import com.example.budgetingapp.security.EmailLinkParameterProvider;
import com.example.budgetingapp.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Setter
public class PasswordEmailServiceImpl implements MessageService {
    private final JavaMailSender mailSender;
    private final EmailLinkParameterProvider emailLinkParameterProvider;
    @Value(RESET_PATH)
    private String resetPath;
    @Value(CONFIRMATION_PATH)
    private String confirmationPath;
    private String subject;
    private String body;

    @Override
    public void sendMessage(String to, String setText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(setText);
        mailSender.send(message);
    }

    @Override
    public void sendActionMessage(String email, String action) {
        switch (action) {
            case RESET -> {
                this.setSubject(INITIATE_RANDOM_PASSWORD_SUBJECT);
                this.setBody(INITIATE_RANDOM_PASSWORD_BODY);
                this.sendMessage(email, formTextForAction(email, resetPath));
            }
            case CONFIRMATION -> {
                this.setSubject(CONFIRM_REGISTRATION_SUBJECT);
                this.setBody(CONFIRM_REGISTRATION_BODY);
                this.sendMessage(email, formTextForAction(email, confirmationPath));
            }
            default -> throw new ActionNotFoundException("Unknown action " + action);
        }
    }

    @Override
    public void sendResetPassword(String email, String randomPassword) {
        this.setSubject(RANDOM_PASSWORD_SUBJECT);
        this.setBody(RANDOM_PASSWORD_BODY);
        String mail = body + System.lineSeparator() + randomPassword;
        this.sendMessage(email, mail);
    }

    private String formTextForAction(String email, String actionPath) {
        emailLinkParameterProvider.formRandomParamTokenPair(email);
        return body + System.lineSeparator() + actionPath
                + emailLinkParameterProvider.getEmailLinkParameter()
                + SPLITERATOR + emailLinkParameterProvider.getToken();
    }
}
