package com.example.budgetingapp.services.utils;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;

import com.example.budgetingapp.entities.tokens.ActionToken;
import com.example.budgetingapp.repositories.ActionTokenRepository;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionTokenUtil {
    private final JwtStrategy jwtStrategy;
    private final ActionTokenRepository actionTokenRepository;

    public String generateActionToken(String email) {
        JwtAbstractUtil actionUtil = jwtStrategy.getStrategy(ACTION);
        ActionToken actionToken = new ActionToken();
        actionToken.setActionToken(actionUtil.generateToken(email));
        actionTokenRepository.save(actionToken);
        return actionToken.getActionToken();
    }
}
