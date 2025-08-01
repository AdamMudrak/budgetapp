package com.example.budgetingapp.security.jwtutils.impl;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACCESS;

import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier(ACCESS)
public class JwtAccessUtil extends JwtAbstractUtil {
    public JwtAccessUtil(@Value("${jwt.secret}") String secretString,
                         @Value("${jwt.access.expiration}") long expiration) {
        super(secretString, expiration);
    }
}
