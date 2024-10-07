package com.example.budgetingapp.security.jwtutils.implementations;

import static com.example.budgetingapp.security.SecurityConstants.JWT_RESET_EXPIRATION;
import static com.example.budgetingapp.security.SecurityConstants.JWT_SECRET;
import static com.example.budgetingapp.security.SecurityConstants.RESET;

import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier(RESET)
public class JwtResetUtil extends JwtAbstractUtil {
    public JwtResetUtil(@Value(JWT_SECRET) String secretString,
                        @Value(JWT_RESET_EXPIRATION) long expiration) {
        super(secretString, expiration);
    }
}
