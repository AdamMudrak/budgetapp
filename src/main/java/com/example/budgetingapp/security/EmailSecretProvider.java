package com.example.budgetingapp.security;

import static com.example.budgetingapp.constants.security.SecurityConstants.EMAIL_SECRET;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EmailSecretProvider {
    private final String emailSecret;

    public EmailSecretProvider(@Autowired Environment environment) {
        emailSecret = environment.getProperty(EMAIL_SECRET);
    }
}
