package com.example.budgetingapp.security.jwtutils.implementations;

import static com.example.budgetingapp.security.SecurityConstants.ACCESS;
import static com.example.budgetingapp.security.SecurityConstants.JWT_ACCESS_EXPIRATION;
import static com.example.budgetingapp.security.SecurityConstants.JWT_SECRET;

import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier(ACCESS)
public class JwtAccessUtil extends JwtAbstractUtil {
    public JwtAccessUtil(@Value(JWT_SECRET) String secretString,
                         @Value(JWT_ACCESS_EXPIRATION) long expiration) {
        super(secretString, expiration);
    }
}