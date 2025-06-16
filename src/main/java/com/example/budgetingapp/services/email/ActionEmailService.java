package com.example.budgetingapp.services.email;

import static com.example.budgetingapp.constants.Constants.SPLITERATOR;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRMATION;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRM_REGISTRATION_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRM_REGISTRATION_SUBJECT;
import static com.example.budgetingapp.constants.security.SecurityConstants.INITIATE_RANDOM_PASSWORD_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.INITIATE_RANDOM_PASSWORD_SUBJECT;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_BODY_2;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_BODY_3;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_SUBJECT;
import static com.example.budgetingapp.constants.security.SecurityConstants.RESET;

import com.example.budgetingapp.exceptions.ActionNotFoundException;
import com.example.budgetingapp.services.utils.EmailLinkParameterProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActionEmailService extends EmailService {
    private final EmailLinkParameterProvider emailLinkParameterProvider;
    @Value("${server.path}")
    private String serverPath;
    @Value("${get.random.password.redirect.link}")
    private String redirectPath;

    public ActionEmailService(EmailLinkParameterProvider emailLinkParameterProvider) {
        this.emailLinkParameterProvider = emailLinkParameterProvider;
    }

    public void sendActionMessage(String toEmail, String action) {
        switch (action) {
            case RESET -> this.sendMessage(toEmail, INITIATE_RANDOM_PASSWORD_SUBJECT,
                    formTextForAction(toEmail, INITIATE_RANDOM_PASSWORD_BODY,
                            serverPath + "/auth/reset-password?"));
            case CONFIRMATION -> this.sendMessage(toEmail, CONFIRM_REGISTRATION_SUBJECT,
                    formTextForAction(toEmail, CONFIRM_REGISTRATION_BODY, serverPath
                            + "/auth/register-success?"));
            default -> throw new ActionNotFoundException("Unknown action " + action);
        }
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

    private String formTextForAction(String toEmail, String body, String actionPath) {
        emailLinkParameterProvider.formRandomParamTokenPair(toEmail);
        return body + System.lineSeparator() + actionPath
                + emailLinkParameterProvider.getEmailLinkParameter()
                + SPLITERATOR + emailLinkParameterProvider.getToken();
    }
}
