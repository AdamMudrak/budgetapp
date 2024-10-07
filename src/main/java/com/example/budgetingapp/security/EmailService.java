package com.example.budgetingapp.security;

import static com.example.budgetingapp.security.SecurityConstants.PATH;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Setter
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailSecretProvider emailSecretProvider;
    private String subject;
    private String body;

    public void sendSimpleEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body + System.lineSeparator() + PATH
                + emailSecretProvider.getEmailSecret()
                + "=" + token);
        mailSender.send(message);
    }
}
