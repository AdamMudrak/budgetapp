package com.example.budgetingapp.services.email;

import static com.example.budgetingapp.constants.security.SecurityConstants.SUPPORT_EMAIL_SUBJECT;

import com.example.budgetingapp.constants.Constants;
import com.example.budgetingapp.dtos.support.request.SupportRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContactUsByEmailService extends EmailService {
    @Value("${support.mail.address}")
    private String supportEmail;

    public void sendMessageToHost(SupportRequestDto contactUsByEmailDto) {
        super.sendMessage(supportEmail, getSubject(contactUsByEmailDto),
                contactUsByEmailDto.message());
    }

    private String getSubject(SupportRequestDto contactUsByEmailDto) {
        return SUPPORT_EMAIL_SUBJECT
                + contactUsByEmailDto.name()
                + Constants.LEFT_PARENTHESIS
                + contactUsByEmailDto.email()
                + Constants.RIGHT_PARENTHESIS;
    }
}
