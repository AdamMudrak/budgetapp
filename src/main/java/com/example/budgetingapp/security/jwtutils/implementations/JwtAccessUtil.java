package com.example.budgetingapp.security.jwtutils.implementations;

import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("jwtAccessUtil")
public class JwtAccessUtil extends JwtAbstractUtil {
    public JwtAccessUtil(String secretString, long expiration) {
        super(secretString, expiration);
    }
}
