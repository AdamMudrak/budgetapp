package com.example.budgetingapp.security.jwtutils.impl;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;

import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier(ACTION)
public class JwtResetUtil extends JwtAbstractUtil {
    public JwtResetUtil(@Value("${jwt.secret}") String secretString,
                        @Value("${jwt.action.expiration}") long expiration) {
        super(secretString, expiration);
    }
}
