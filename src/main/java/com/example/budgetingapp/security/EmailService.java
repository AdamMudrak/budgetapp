package com.example.budgetingapp.security;

import static com.example.budgetingapp.constants.Constants.SPLITERATOR;
import static com.example.budgetingapp.constants.security.SecurityConstants.PATH_PROPERTY;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Setter
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailSecretProvider emailSecretProvider;
    @Value(PATH_PROPERTY)
    private String actionPath;
    private String subject;
    private String body;

    public void sendSimpleEmail(String to, String setText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(setText);
        mailSender.send(message);
    }

    public String formTextForAction(String token) {
        return body + System.lineSeparator() + actionPath
                + emailSecretProvider.getEmailSecret()
                + SPLITERATOR + token;
    }

    public String formTextForNewPassword(String password) {
        return body + System.lineSeparator()
                + password;
    }
}
