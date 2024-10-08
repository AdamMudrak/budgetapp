package com.example.budgetingapp.security.jwtutils;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACCESS;
import static com.example.budgetingapp.constants.security.SecurityConstants.REFRESH;
import static com.example.budgetingapp.constants.security.SecurityConstants.RESET;

import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JwtStrategy {
    private final JwtAbstractUtil accessUtil;
    private final JwtAbstractUtil refreshUtil;
    private final JwtAbstractUtil resetUtil;

    public JwtStrategy(@Qualifier(ACCESS) JwtAbstractUtil accessUtil,
                      @Qualifier(REFRESH) JwtAbstractUtil refreshUtil,
                      @Qualifier(RESET) JwtAbstractUtil resetUtil) {
        this.accessUtil = accessUtil;
        this.refreshUtil = refreshUtil;
        this.resetUtil = resetUtil;
    }

    public JwtAbstractUtil getStrategy(String key) {
        return switch (key) {
            case ACCESS -> accessUtil;
            case REFRESH -> refreshUtil;
            case RESET -> resetUtil;
            default -> throw new JwtException("No such Jwt util");
        };
    }
}
