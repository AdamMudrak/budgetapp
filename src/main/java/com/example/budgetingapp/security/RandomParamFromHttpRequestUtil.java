package com.example.budgetingapp.security;

import com.example.budgetingapp.exceptions.ActionNotFoundException;
import com.example.budgetingapp.repositories.actiontoken.ActionTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class RandomParamFromHttpRequestUtil {
    private final ActionTokenRepository actionTokenRepository;
    private String randomParam;
    private String token;

    public void getSecretParam(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            setRandomParam(entry.getKey());
            break;
        }
    }

    public String getTokenFromRepo(String randomParam) {
        setToken(actionTokenRepository
                .findByParameter(randomParam)
                .orElseThrow(() -> new ActionNotFoundException(
                        "No such request was found... The link might be expired or forged"))
                .getActionToken());
        return token;
    }
}
