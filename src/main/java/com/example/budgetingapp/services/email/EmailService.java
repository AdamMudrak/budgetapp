package com.example.budgetingapp.services.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LogManager.getLogger(EmailService.class);
    @Value("${sender.mail.address}")
    protected String senderEmail;
    @Value("${resend.api.key}")
    private String resendApiKey;

    public void sendMessage(String toEmail, String subject, String body) {
        Resend resend = new Resend(resendApiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(senderEmail)
                .to(toEmail)
                .subject(subject)
                .text(body)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            logger.info("Response id: {}",
                    data.getId());
        } catch (ResendException e) {
            logger.error(e.getMessage());
        }
    }
}
