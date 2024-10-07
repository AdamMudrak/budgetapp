package com.example.budgetingapp.security.jwtutils;

import static com.example.budgetingapp.security.SecurityConstants.ACCESS;
import static com.example.budgetingapp.security.SecurityConstants.JWT_ACCESS_EXPIRATION;
import static com.example.budgetingapp.security.SecurityConstants.JWT_REFRESH_EXPIRATION;
import static com.example.budgetingapp.security.SecurityConstants.JWT_RESET_EXPIRATION;
import static com.example.budgetingapp.security.SecurityConstants.JWT_SECRET;
import static com.example.budgetingapp.security.SecurityConstants.REFRESH;
import static com.example.budgetingapp.security.SecurityConstants.RESET;

import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.implementations.JwtImplUtil;
import io.jsonwebtoken.JwtException;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.env.Environment;

@Getter
@Setter
public class JwtStrategy {
    private JwtAbstractUtil abstractUtil;
    private String secretString;
    private long expiration;

    public JwtAbstractUtil getJwtUtilByKey(Environment environment,
                                           String key) {
        setSecretString(environment.getProperty(JWT_SECRET));
        switch (key) {
            case ACCESS -> {
                setExpiration(Long.parseLong(Objects.requireNonNull(
                        environment.getProperty(JWT_ACCESS_EXPIRATION))));
                return new JwtImplUtil(secretString, expiration);
            }
            case REFRESH -> {
                setExpiration(Long.parseLong(Objects.requireNonNull(
                        environment.getProperty(JWT_REFRESH_EXPIRATION))));
                return new JwtImplUtil(secretString, expiration);
            }
            case RESET -> {
                setExpiration(Long.parseLong(Objects.requireNonNull(
                        environment.getProperty(JWT_RESET_EXPIRATION))));
                return new JwtImplUtil(secretString, expiration);
            }
            default -> throw new JwtException("No such util");
        }
    }
}
