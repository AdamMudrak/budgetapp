package com.example.budgetingapp.services.email;

import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRM_REGISTRATION_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRM_REGISTRATION_SUBJECT;

import com.example.budgetingapp.services.utils.ActionTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterConfirmEmailService extends EmailService {
    private final ActionTokenUtil actionTokenUtil;
    @Value("${server.path}")
    private String serverPath;

    public void sendRegisterConfirmEmail(String toEmail) {
        this.sendMessage(toEmail, CONFIRM_REGISTRATION_SUBJECT,
                CONFIRM_REGISTRATION_BODY
                        + System.lineSeparator()
                        + serverPath + "/auth/register-success?token="
                        + actionTokenUtil.generateActionToken(toEmail));
    }
}
