package com.example.budgetingapp.security;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACCESS;
import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;
import static com.example.budgetingapp.constants.security.SecurityConstants.INITIATE_RANDOM_PASSWORD_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.INITIATE_RANDOM_PASSWORD_SUBJECT;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_STRENGTH;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_SUBJECT;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_STRING_BASE;
import static com.example.budgetingapp.constants.security.SecurityConstants.SUCCESSFUL_CHANGE_MESSAGE;
import static com.example.budgetingapp.constants.security.SecurityConstants.SUCCESSFUL_RESET_MSG;
import static com.example.budgetingapp.constants.security.SecurityConstants.SUCCESS_EMAIL;

import com.example.budgetingapp.dtos.user.request.UserLoginRequestDto;
import com.example.budgetingapp.dtos.user.request.UserSetNewPasswordRequestDto;
import com.example.budgetingapp.dtos.user.response.UserLoginResponseDto;
import com.example.budgetingapp.entities.ActionToken;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.LinkExpiredException;
import com.example.budgetingapp.exceptions.PasswordMismatch;
import com.example.budgetingapp.repositories.actiontoken.ActionTokenRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.security.jwtutils.JwtStrategy;
import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import io.jsonwebtoken.JwtException;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ActionTokenRepository actionTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtStrategy jwtStrategy;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDto.email(), requestDto.password()));
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACCESS);
        String token = jwtAbstractUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }

    public String initiatePasswordReset(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(
                    "User with email " + email + " was not found");
        }

        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACTION);
        ActionToken actionToken = new ActionToken();
        actionToken.setActionToken(jwtAbstractUtil.generateToken(email));
        actionTokenRepository.save(actionToken);
        sendInitiatePasswordReset(email, actionToken.getActionToken());
        return SUCCESS_EMAIL;
    }

    public String resetPassword(String token) {
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACCESS);
        try {
            jwtAbstractUtil.isValidToken(token);
        } catch (JwtException e) {
            throw new LinkExpiredException("This link is expired. Please, submit another "
                    + " \"forgot password\" request");
        }

        Optional<ActionToken> actionToken = actionTokenRepository.findByActionToken(token);
        if (actionToken.isEmpty()) {
            throw new EntityNotFoundException("No reset request was found by this link");
        }
        actionTokenRepository.deleteById(actionToken.get().getId());
        String email = jwtAbstractUtil.getUsername(actionToken.get().getActionToken());
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with email " + email + " was not found");
        }

        String randomPassword = generateRandomString();
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(randomPassword));
        userRepository.save(user);
        sendResetPassword(email, randomPassword);
        return SUCCESSFUL_RESET_MSG;
    }

    public String changePassword(String token,
                                 UserSetNewPasswordRequestDto userSetNewPasswordRequestDto) {
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACCESS);
        String email = jwtAbstractUtil.getUsername(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        optionalUser.orElseThrow(() -> new EntityNotFoundException("Can't find user with email "
                + email));
        if (!isCurrentPasswordValid(optionalUser.get(), userSetNewPasswordRequestDto)) {
            throw new PasswordMismatch("Wrong password. Try resetting "
                    + "password and using a new random password");
        }
        optionalUser.get().setPassword(passwordEncoder
                .encode(userSetNewPasswordRequestDto.newPassword()));
        userRepository.save(optionalUser.get());
        return SUCCESSFUL_CHANGE_MESSAGE;
    }

    private String generateRandomString() {
        String characters = RANDOM_STRING_BASE;
        StringBuilder randomString =
                new StringBuilder(RANDOM_PASSWORD_STRENGTH);
        Random random = new Random();
        for (int i = 0; i < RANDOM_PASSWORD_STRENGTH; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }
        return randomString.toString();
    }

    private boolean isCurrentPasswordValid(User user,
            UserSetNewPasswordRequestDto userSetNewPasswordRequestDto) {
        return passwordEncoder
                .matches(userSetNewPasswordRequestDto.currentPassword(), user.getPassword());
    }

    private void sendInitiatePasswordReset(String email, String actionToken) {
        emailService.setSubject(INITIATE_RANDOM_PASSWORD_SUBJECT);
        emailService.setBody(INITIATE_RANDOM_PASSWORD_BODY);
        emailService.sendSimpleEmail(email,
                emailService.formTextForAction(actionToken));
    }

    private void sendResetPassword(String email, String randomPassword) {
        emailService.setSubject(RANDOM_PASSWORD_SUBJECT);
        emailService.setBody(RANDOM_PASSWORD_BODY);
        emailService.sendSimpleEmail(email, emailService.formTextForNewPassword(randomPassword));
    }
}
