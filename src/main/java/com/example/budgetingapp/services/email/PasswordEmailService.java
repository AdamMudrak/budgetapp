package com.example.budgetingapp.services.email;

import static com.example.budgetingapp.constants.security.SecurityConstants.INITIATE_RANDOM_PASSWORD_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.INITIATE_RANDOM_PASSWORD_SUBJECT;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_BODY_2;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_BODY_3;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_SUBJECT;

import com.example.budgetingapp.services.utils.ActionTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEmailService extends EmailService {
    private final ActionTokenUtil actionTokenUtil;
    @Value("${server.path}")
    private String serverPath;
    @Value("${get.random.password.redirect.link}")
    private String redirectPath;

    public void sendInitiatePasswordReset(String toEmail) {
        this.sendMessage(toEmail, INITIATE_RANDOM_PASSWORD_SUBJECT,
                    INITIATE_RANDOM_PASSWORD_BODY
                        + System.lineSeparator()
                        + serverPath + "/auth/reset-password?token="
                        + actionTokenUtil.generateActionToken(toEmail));
    }

    public void sendResetPassword(String toEmail, String randomPassword) {
        this.sendMessage(toEmail, RANDOM_PASSWORD_SUBJECT,
                    RANDOM_PASSWORD_BODY
                            + System.lineSeparator()
                            + randomPassword
                            + System.lineSeparator()
                            + RANDOM_PASSWORD_BODY_2
                            + System.lineSeparator()
                            + redirectPath
                            + System.lineSeparator()
                            + RANDOM_PASSWORD_BODY_3);
    }
}
