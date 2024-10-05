package com.example.budgetingapp.security;

import static com.example.budgetingapp.security.SecurityConstants.REFRESH;
import static com.example.budgetingapp.security.SecurityConstants.RESET;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtRefResUtil {
    private final JwtAccUtil jwtAccUtil;

    public void acquireExpiration(String tokenType, Environment environment) {
        switch (tokenType) {
            case RESET -> jwtAccUtil.setExpiration(Long.parseLong(Objects.requireNonNull(
                    environment.getProperty("jwt.reset-expiration"))));
            case REFRESH -> jwtAccUtil.setExpiration(Long.parseLong(Objects.requireNonNull(
                    environment.getProperty("jwt.refresh-expiration"))));
            //TODO SHOULD BE SERVER SPECIFIC EXCEPTION
            default -> throw new RuntimeException("Could not understand JWT type");
        }
    }
}
