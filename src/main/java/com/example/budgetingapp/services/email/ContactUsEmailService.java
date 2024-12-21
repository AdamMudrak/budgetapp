package com.example.budgetingapp.services.email;

import static com.example.budgetingapp.constants.Constants.SUPPORT_EMAIL;
import static com.example.budgetingapp.constants.Constants.SUPPORT_EMAIL_SUBJECT;

import com.example.budgetingapp.constants.Constants;
import com.example.budgetingapp.dtos.emails.ContactUsByEmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactUsEmailService extends EmailService {
    @Value(SUPPORT_EMAIL)
    private String supportEmail;

    public ContactUsEmailService(JavaMailSender mailSender) {
        super(mailSender);
    }

    public void sendMessageToHost(ContactUsByEmailDto contactUsByEmailDto) {
        super.sendMessage(supportEmail, getSubject(contactUsByEmailDto),
                contactUsByEmailDto.message());
    }

    String getSubject(ContactUsByEmailDto contactUsByEmailDto) {
        return SUPPORT_EMAIL_SUBJECT
                + contactUsByEmailDto.name()
                + System.lineSeparator()
                + Constants.LEFT_PARENTHESIS
                + contactUsByEmailDto.email()
                + Constants.RIGHT_PARENTHESIS;
    }
}
