package com.example.budgetingapp.security.authentication;

import static com.example.budgetingapp.constants.Constants.EMAIL;
import static com.example.budgetingapp.constants.Constants.TELEGRAM_PHONE_NUMBER;
import static com.example.budgetingapp.constants.security.SecurityConstants.ACCESS;
import static com.example.budgetingapp.constants.security.SecurityConstants.BEGIN_INDEX;
import static com.example.budgetingapp.constants.security.SecurityConstants.PASSWORD_SET_SUCCESSFULLY;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_STRENGTH;
import static com.example.budgetingapp.constants.security.SecurityConstants.REFRESH;
import static com.example.budgetingapp.constants.security.SecurityConstants.REFRESH_TOKEN;
import static com.example.budgetingapp.constants.security.SecurityConstants.REGISTERED_BUT_NOT_ACTIVATED;
import static com.example.budgetingapp.constants.security.SecurityConstants.SUCCESS_EMAIL;

import com.example.budgetingapp.dtos.users.request.SetNewPasswordDto;
import com.example.budgetingapp.dtos.users.request.userlogindtos.InnerUserLoginDto;
import com.example.budgetingapp.dtos.users.request.userlogindtos.UserEmailLoginDto;
import com.example.budgetingapp.dtos.users.request.userlogindtos.UserTelegramLoginDto;
import com.example.budgetingapp.dtos.users.response.AccessTokenDto;
import com.example.budgetingapp.dtos.users.response.StartPasswordResetDto;
import com.example.budgetingapp.dtos.users.response.UserLoginDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.LoginException;
import com.example.budgetingapp.exceptions.PasswordMismatch;
import com.example.budgetingapp.mappers.UserMapper;
import com.example.budgetingapp.repositories.UserRepository;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import com.example.budgetingapp.services.email.PasswordEmailService;
import com.example.budgetingapp.services.email.RegisterConfirmEmailService;
import com.example.budgetingapp.services.utils.ParamFromHttpRequestUtil;
import com.example.budgetingapp.services.utils.RandomStringUtil;
import com.example.budgetingapp.services.utils.RedirectUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final ParamFromHttpRequestUtil requestUtil;
    private final RedirectUtil redirectUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtStrategy jwtStrategy;
    private final PasswordEmailService passwordEmailService;
    private final RegisterConfirmEmailService registerConfirmEmailService;
    private final RandomStringUtil randomStringUtil;
    @Value("${password.reset.confirmation.link}")
    private String happyRedirectPath;
    @Value("${action.error.link}")
    private String actionErrorPath;

    public UserLoginDto authenticateTelegram(UserTelegramLoginDto requestDto) {
        InnerUserLoginDto innerUserLoginRequestDto = interprete(requestDto);
        User currentUser = isCreated(innerUserLoginRequestDto.userName(), TELEGRAM_PHONE_NUMBER);
        isEnabled(currentUser);
        return getTokens(innerUserLoginRequestDto, TELEGRAM_PHONE_NUMBER);
    }

    public UserLoginDto authenticateEmail(UserEmailLoginDto requestDto) {
        InnerUserLoginDto innerUserLoginRequestDto = interprete(requestDto);
        User currentUser = isCreated(innerUserLoginRequestDto.userName(), EMAIL);
        isEnabled(currentUser);
        return getTokens(innerUserLoginRequestDto, EMAIL);
    }

    public StartPasswordResetDto initiatePasswordReset(String email) {
        User currentUser = isCreated(email);
        isEnabled(currentUser);
        passwordEmailService.sendInitiatePasswordReset(email);
        return new StartPasswordResetDto(SUCCESS_EMAIL);
    }

    public ResponseEntity<Void> confirmResetPassword(HttpServletRequest httpServletRequest) {
        String token = requestUtil.parseTokenFromParam(httpServletRequest);
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACCESS);
        try {
            jwtAbstractUtil.isValidToken(token);
        } catch (Exception e) {
            redirectUtil.redirect(actionErrorPath);
        }
        String email = jwtAbstractUtil.getUsername(token);
        String randomPassword = randomStringUtil.generateRandomString(RANDOM_PASSWORD_STRENGTH);
        User user = userRepository.findByUserName(email).orElseThrow(() ->
                new EntityNotFoundException("User with email " + email + " was not found"));
        user.setPassword(passwordEncoder.encode(randomPassword));
        userRepository.save(user);
        passwordEmailService.sendResetPassword(email, randomPassword);
        return redirectUtil.redirect(happyRedirectPath);
    }

    public StartPasswordResetDto changePassword(HttpServletRequest httpServletRequest,
                                                SetNewPasswordDto userSetNewPasswordRequestDto) {
        String token = getToken(httpServletRequest);
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
        return new StartPasswordResetDto(PASSWORD_SET_SUCCESSFULLY);
    }

    public AccessTokenDto refreshToken(HttpServletRequest httpServletRequest) {
        Cookie cookie = findRefreshCookie(httpServletRequest);
        JwtAbstractUtil refreshUtil = jwtStrategy.getStrategy(REFRESH);
        JwtAbstractUtil accessUtil = jwtStrategy.getStrategy(ACCESS);
        String refreshToken = cookie.getValue();
        if (refreshUtil.isValidToken(refreshToken)) {
            String username = refreshUtil.getUsername(refreshToken);
            return new AccessTokenDto(accessUtil.generateToken(username));
        }
        throw new LoginException("Something went wrong with your access");
    }

    private boolean isCurrentPasswordValid(User user,
                                       SetNewPasswordDto userSetNewPasswordRequestDto) {
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
            registerConfirmEmailService.sendRegisterConfirmEmail(user.getUsername());
            throw new LoginException(REGISTERED_BUT_NOT_ACTIVATED);
        }
    }

    private Cookie findRefreshCookie(HttpServletRequest httpServletRequest) {
        return Arrays.stream(httpServletRequest.getCookies())
                .filter(refreshCookie -> refreshCookie.getName().equals(REFRESH_TOKEN))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find RT in cookies"));
    }

    private InnerUserLoginDto interprete(
            UserTelegramLoginDto userTelegramLoginRequestDto) {
        return userMapper.toInnerUserDto(userTelegramLoginRequestDto);
    }

    private InnerUserLoginDto interprete(
            UserEmailLoginDto userEmailLoginRequestDto) {
        return userMapper.toInnerUserDto(userEmailLoginRequestDto);
    }

    private UserLoginDto getTokens(InnerUserLoginDto innerUserLoginRequestDto,
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
        return new UserLoginDto(accessToken, refreshToken);
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(BEGIN_INDEX);
        }
        return null;
    }
}
