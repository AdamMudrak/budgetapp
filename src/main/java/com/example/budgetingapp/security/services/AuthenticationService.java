package com.example.budgetingapp.security.services;

import static com.example.budgetingapp.constants.Constants.EMAIL;
import static com.example.budgetingapp.constants.Constants.TELEGRAM_PHONE_NUMBER;
import static com.example.budgetingapp.constants.redirects.RedirectConstants.PASSWORD_RESET_CONFIRMATION_LINK;
import static com.example.budgetingapp.constants.security.SecurityConstants.ACCESS;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRMATION;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_STRENGTH;
import static com.example.budgetingapp.constants.security.SecurityConstants.REFRESH;
import static com.example.budgetingapp.constants.security.SecurityConstants.REFRESH_TOKEN;
import static com.example.budgetingapp.constants.security.SecurityConstants.REGISTERED_BUT_NOT_ACTIVATED;
import static com.example.budgetingapp.constants.security.SecurityConstants.RESET;
import static com.example.budgetingapp.constants.security.SecurityConstants.SUCCESSFUL_CHANGE_MESSAGE;
import static com.example.budgetingapp.constants.security.SecurityConstants.SUCCESS_EMAIL;

import com.example.budgetingapp.constants.controllers.AuthControllerConstants;
import com.example.budgetingapp.constants.security.SecurityConstants;
import com.example.budgetingapp.dtos.users.request.UserSetNewPasswordRequestDto;
import com.example.budgetingapp.dtos.users.request.userloginrequestdtos.InnerUserLoginRequestDto;
import com.example.budgetingapp.dtos.users.request.userloginrequestdtos.UserEmailLoginRequestDto;
import com.example.budgetingapp.dtos.users.request.userloginrequestdtos.UserTelegramLoginRequestDto;
import com.example.budgetingapp.dtos.users.response.AccessTokenResponseDto;
import com.example.budgetingapp.dtos.users.response.UserLoginResponseDto;
import com.example.budgetingapp.dtos.users.response.UserPasswordResetResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.tokens.ParamToken;
import com.example.budgetingapp.exceptions.forbidden.LoginException;
import com.example.budgetingapp.exceptions.gone.LinkExpiredException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.unauthorized.PasswordMismatch;
import com.example.budgetingapp.mappers.UserMapper;
import com.example.budgetingapp.repositories.paramtoken.ParamTokenRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.security.RandomStringUtil;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import com.example.budgetingapp.services.RedirectUtil;
import com.example.budgetingapp.services.email.PasswordEmailService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final RedirectUtil redirectUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtStrategy jwtStrategy;
    private final PasswordEmailService passwordEmailService;
    private final RandomStringUtil randomStringUtil;
    private final ParamTokenRepository paramTokenRepository;

    public UserLoginResponseDto authenticateTelegram(UserTelegramLoginRequestDto requestDto) {
        InnerUserLoginRequestDto innerUserLoginRequestDto = interprete(requestDto);
        User currentUser = isCreated(innerUserLoginRequestDto.userName(), TELEGRAM_PHONE_NUMBER);
        isEnabled(currentUser);
        return getTokens(innerUserLoginRequestDto, TELEGRAM_PHONE_NUMBER);
    }

    public UserLoginResponseDto authenticateEmail(UserEmailLoginRequestDto requestDto) {
        InnerUserLoginRequestDto innerUserLoginRequestDto = interprete(requestDto);
        User currentUser = isCreated(innerUserLoginRequestDto.userName(), EMAIL);
        isEnabled(currentUser);
        return getTokens(innerUserLoginRequestDto, EMAIL);
    }

    public UserPasswordResetResponseDto initiatePasswordReset(String email) {
        User currentUser = isCreated(email);
        isEnabled(currentUser);
        passwordEmailService.sendActionMessage(email, RESET);
        return new UserPasswordResetResponseDto(SUCCESS_EMAIL);
    }

    public ResponseEntity<Void> confirmResetPassword(String token) {
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
        return redirectUtil.redirect(PASSWORD_RESET_CONFIRMATION_LINK);
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

    private User isCreated(String userName, String userNameType) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new LoginException(
                        "Either " + userNameType + " or password is invalid"));
    }

    private User isCreated(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with email " + userName + " was not found"));
    }

    private void isEnabled(User user) {
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

    private InnerUserLoginRequestDto interprete(
            UserTelegramLoginRequestDto userTelegramLoginRequestDto) {
        return userMapper.toInnerUserDto(userTelegramLoginRequestDto);
    }

    private InnerUserLoginRequestDto interprete(
            UserEmailLoginRequestDto userEmailLoginRequestDto) {
        return userMapper.toInnerUserDto(userEmailLoginRequestDto);
    }

    private UserLoginResponseDto getTokens(InnerUserLoginRequestDto innerUserLoginRequestDto,
                                           String userNameType) {
        final Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(
                        new UsernamePasswordAuthenticationToken(
                        innerUserLoginRequestDto.userName(), innerUserLoginRequestDto.password()));

        } catch (AuthenticationException authenticationException) {
            throw new LoginException("Either " + userNameType + " or password is invalid");
        }
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACCESS);
        String accessToken = jwtAbstractUtil.generateToken(authentication.getName());
        jwtAbstractUtil = jwtStrategy.getStrategy(REFRESH);
        String refreshToken = jwtAbstractUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(accessToken, refreshToken);
    }
}
