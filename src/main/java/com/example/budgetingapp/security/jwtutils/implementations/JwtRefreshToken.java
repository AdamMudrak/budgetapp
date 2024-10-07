package com.example.budgetingapp.security.jwtutils.implementations;

import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import org.springframework.stereotype.Component;

@Component
public class JwtRefreshToken extends JwtAbstractUtil {
    public JwtRefreshToken(String secretString, long expiration) {
        super(secretString, expiration);
    }
}
