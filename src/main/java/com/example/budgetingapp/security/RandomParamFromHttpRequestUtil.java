package com.example.budgetingapp.security;

import com.example.budgetingapp.entities.ParamToken;
import com.example.budgetingapp.exceptions.ActionNotFoundException;
import com.example.budgetingapp.repositories.paramtoken.ParamTokenRepository;
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
    private final ParamTokenRepository paramTokenRepository;
    private String randomParameter;
    private String token;

    public void parseRandomParameterAndToken(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            setRandomParameter(entry.getKey());
            setToken(entry.getValue()[0]);
            break;
        }
    }

    public String getTokenFromRepo(String randomParam, String token) {
        ParamToken paramToken = paramTokenRepository
                .findByParameterAndActionToken(randomParam, token)
                .orElseThrow(() -> new ActionNotFoundException(
                        "No such request was found... The link might be expired or forged"));
        setToken(paramToken.getActionToken());
        return token;
    }
}