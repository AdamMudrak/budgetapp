package com.example.budgetingapp.security.services;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACCESS;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRMATION;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_STRENGTH;
import static com.example.budgetingapp.constants.security.SecurityConstants.REFRESH;
import static com.example.budgetingapp.constants.security.SecurityConstants.REFRESH_TOKEN;
import static com.example.budgetingapp.constants.security.SecurityConstants.REGISTERED_BUT_NOT_ACTIVATED;
import static com.example.budgetingapp.constants.security.SecurityConstants.RESET;
import static com.example.budgetingapp.constants.security.SecurityConstants.SUCCESSFUL_CHANGE_MESSAGE;
import static com.example.budgetingapp.constants.security.SecurityConstants.SUCCESSFUL_RESET_MSG;
import static com.example.budgetingapp.constants.security.SecurityConstants.SUCCESS_EMAIL;

import com.example.budgetingapp.constants.controllers.AuthControllerConstants;
import com.example.budgetingapp.constants.security.SecurityConstants;
import com.example.budgetingapp.dtos.user.request.UserLoginRequestDto;
import com.example.budgetingapp.dtos.user.request.UserSetNewPasswordRequestDto;
import com.example.budgetingapp.dtos.user.response.AccessTokenResponseDto;
import com.example.budgetingapp.dtos.user.response.UserLoginResponseDto;
import com.example.budgetingapp.dtos.user.response.UserPasswordResetResponseDto;
import com.example.budgetingapp.entities.tokens.ParamToken;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.LinkExpiredException;
import com.example.budgetingapp.exceptions.LoginException;
import com.example.budgetingapp.exceptions.PasswordMismatch;
import com.example.budgetingapp.repositories.paramtoken.ParamTokenRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.security.RandomStringUtil;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import com.example.budgetingapp.services.impl.PasswordEmailService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtStrategy jwtStrategy;
    private final PasswordEmailService passwordEmailService;
    private final RandomStringUtil randomStringUtil;
    private final ParamTokenRepository paramTokenRepository;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        isCreatedAndEnabled(requestDto);
        final Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDto.userName(), requestDto.password()));
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACCESS);
        String accessToken = jwtAbstractUtil.generateToken(authentication.getName());
        jwtAbstractUtil = jwtStrategy.getStrategy(REFRESH);
        String refreshToken = jwtAbstractUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(accessToken, refreshToken);
    }

    public UserPasswordResetResponseDto initiatePasswordReset(String email) {
        Optional<User> user = userRepository.findByUserName(email);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(
                    "User with email " + email + " was not found");
        }
        passwordEmailService.sendActionMessage(email, RESET);
        return new UserPasswordResetResponseDto(SUCCESS_EMAIL);
    }

    public UserPasswordResetResponseDto confirmResetPassword(String token) {
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACCESS);
        try {
            jwtAbstractUtil.isValidToken(token);
        } catch (JwtException e) {
            throw new LinkExpiredException("This link is expired. Please, submit another "
                    + " \"forgot password\" request");
        }
        String email = getEmailFromTokenSecure(token, jwtAbstractUtil);
        String randomPassword = randomStringUtil.generateRandomString(RANDOM_PASSWORD_STRENGTH);
        User user = userRepository.findByUserName(email).orElseThrow(() ->
                new EntityNotFoundException("User with email " + email + " was not found"));
        user.setPassword(passwordEncoder.encode(randomPassword));
        userRepository.save(user);
        passwordEmailService.sendResetPassword(email, randomPassword);
        return new UserPasswordResetResponseDto(SUCCESSFUL_RESET_MSG);
    }

    public UserPasswordResetResponseDto changePassword(HttpServletRequest httpServletRequest,
                                       UserSetNewPasswordRequestDto userSetNewPasswordRequestDto) {
        String token = parseToken(httpServletRequest);
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACCESS);
        String email = jwtAbstractUtil.getUsername(token);
        User user = userRepository.findByUserName(email).orElseThrow(() ->
                new EntityNotFoundException("User with email " + email + " was not found"));
        if (!isCurrentPasswordValid(user, userSetNewPasswordRequestDto)) {
            throw new PasswordMismatch("Wrong password. Try resetting "
                    + "password and using a new random password");
        }
        user.setPassword(passwordEncoder
                .encode(userSetNewPasswordRequestDto.newPassword()));
        userRepository.save(user);
        return new UserPasswordResetResponseDto(SUCCESSFUL_CHANGE_MESSAGE);
    }

    public AccessTokenResponseDto refreshToken(HttpServletRequest httpServletRequest) {
        Cookie cookie = findRefreshCookie(httpServletRequest);
        JwtAbstractUtil refreshUtil = jwtStrategy.getStrategy(REFRESH);
        JwtAbstractUtil accessUtil = jwtStrategy.getStrategy(ACCESS);
        String refreshToken = cookie.getValue();
        if (refreshUtil.isValidToken(refreshToken)) {
            String username = refreshUtil.getUsername(refreshToken);
            return new AccessTokenResponseDto(accessUtil.generateToken(username));
        }
        throw new LoginException("Something went wrong with your access");
    }

    private boolean isCurrentPasswordValid(User user,
                                       UserSetNewPasswordRequestDto userSetNewPasswordRequestDto) {
        return passwordEncoder
                .matches(userSetNewPasswordRequestDto.currentPassword(), user.getPassword());
    }

    private void isCreatedAndEnabled(UserLoginRequestDto requestDto) {
        User user = userRepository.findByUserName(requestDto.userName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with email " + requestDto.userName() + " was not found"));
        if (!user.isEnabled()) {
            passwordEmailService.sendActionMessage(user.getUsername(), CONFIRMATION);
            throw new LoginException(REGISTERED_BUT_NOT_ACTIVATED);
        }
    }

    private String getEmailFromTokenSecure(String token, JwtAbstractUtil jwtAbstractUtil) {
        ParamToken paramToken = paramTokenRepository.findByActionToken(token).orElseThrow(()
                -> new EntityNotFoundException(
                "No such request was found... The link might be expired or forged"));
        String email = jwtAbstractUtil.getUsername(paramToken.getActionToken());
        paramTokenRepository.deleteById(paramToken.getId());
        return email;
    }

    private Cookie findRefreshCookie(HttpServletRequest httpServletRequest) {
        return Arrays.stream(httpServletRequest.getCookies())
                .filter(refreshCookie -> refreshCookie.getName().equals(REFRESH_TOKEN))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find RT in cookies"));
    }

    private String parseToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken
                .startsWith(AuthControllerConstants.BEARER)) {
            bearerToken = bearerToken.substring(SecurityConstants.BEGIN_INDEX);
        }
        return bearerToken;
    }
}
