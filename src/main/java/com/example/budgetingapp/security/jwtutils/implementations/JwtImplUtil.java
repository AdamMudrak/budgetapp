package com.example.budgetingapp.security.jwtutils.implementations;

import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import org.springframework.stereotype.Component;

@Component
public class JwtImplUtil extends JwtAbstractUtil {
    public JwtImplUtil(String secretString, long expiration) {
        super(secretString, expiration);
    }
}
