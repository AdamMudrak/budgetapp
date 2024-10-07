package com.example.budgetingapp.security.jwtutils.implementations;

import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import org.springframework.stereotype.Component;

@Component
public class JwtResetUtil extends JwtAbstractUtil {
    public JwtResetUtil(String secretString, long expiration) {
        super(secretString, expiration);
    }
}
