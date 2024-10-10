package com.example.budgetingapp.security;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_LINK_STRENGTH;

import com.example.budgetingapp.entities.ParamToken;
import com.example.budgetingapp.repositories.paramtoken.ParamTokenRepository;
import com.example.budgetingapp.security.jwtutils.JwtStrategy;
import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class EmailLinkParameterProvider {
    private final RandomStringUtil randomStringUtil;
    private final ParamTokenRepository paramTokenRepository;
    private final JwtStrategy jwtStrategy;
    private String emailLinkParameter;
    private String token;

    void formRandomParamTokenPair(String email) {
        setEmailLinkParameter(randomStringUtil.generateRandomString(RANDOM_LINK_STRENGTH));
        JwtAbstractUtil abstractUtil = jwtStrategy.getStrategy(ACTION);
        setToken(abstractUtil.generateToken(email));

        ParamToken paramToken = new ParamToken();
        paramToken.setParameter(emailLinkParameter);
        paramToken.setActionToken(token);
        paramTokenRepository.save(paramToken);
    }
}
