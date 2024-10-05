package com.example.budgetingapp.security;

import static com.example.budgetingapp.security.SecurityConstants.RESET_PASSWORD_BODY;
import static com.example.budgetingapp.security.SecurityConstants.RESET_PASSWORD_SUBJECT;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(RESET_PASSWORD_SUBJECT);
        message.setText(RESET_PASSWORD_BODY + System.lineSeparator() + token);
        mailSender.send(message);
    }
}
