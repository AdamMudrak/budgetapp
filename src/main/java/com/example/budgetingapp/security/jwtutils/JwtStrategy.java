package com.example.budgetingapp.security.jwtutils;

import static com.example.budgetingapp.security.SecurityConstants.ACCESS;
import static com.example.budgetingapp.security.SecurityConstants.REFRESH;
import static com.example.budgetingapp.security.SecurityConstants.RESET;

import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.implementations.JwtAccessUtil;
import io.jsonwebtoken.JwtException;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class JwtStrategy {
    private JwtAbstractUtil abstractUtil;
    private String secretString;
    private long expiration;

    public JwtAbstractUtil getJwtUtilByKey(Environment environment,
                                           String key) {
        setSecretString(environment.getProperty("jwt.secret"));
        switch (key) {
            case ACCESS -> {
                setExpiration(Long.parseLong(Objects.requireNonNull(
                        environment.getProperty("jwt.access-expiration"))));
                setAbstractUtil(new JwtAccessUtil(secretString, expiration));
                return getAbstractUtil();
            }
            case REFRESH -> {
                setExpiration(Long.parseLong(Objects.requireNonNull(
                        environment.getProperty("jwt.refresh-expiration"))));
                setAbstractUtil(new JwtAccessUtil(secretString, expiration));
                return getAbstractUtil();
            }
            case RESET -> {
                setExpiration(Long.parseLong(Objects.requireNonNull(
                        environment.getProperty("jwt.reset-expiration"))));
                setAbstractUtil(new JwtAccessUtil(secretString, expiration));
                return getAbstractUtil();
            }
            default -> throw new JwtException("No such util");
        }
    }
}
