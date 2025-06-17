package com.example.budgetingapp.services.utils;

import com.example.budgetingapp.exceptions.ActionNotFoundException;
import com.example.budgetingapp.repositories.ActionTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParamFromHttpRequestUtil {
    private final ActionTokenRepository actionTokenRepository;

    public String parseTokenFromParam(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (token != null && !token.isBlank()) {
            if (!actionTokenRepository.existsByActionToken(token)) {
                throw new ActionNotFoundException(
                        "No such request was found... The link might be expired or forged");
            } else {
                actionTokenRepository.deleteByActionToken(token);
                return token;
            }
        } else {
            throw new ActionNotFoundException(
                    "Wasn't able to parse link...Might be expired or forged");
        }
    }
}
