package com.example.budgetingapp.security;

import static com.example.budgetingapp.security.SecurityConstants.RESET;

import com.example.budgetingapp.dtos.user.request.UserLoginRequestDto;
import com.example.budgetingapp.dtos.user.request.UserResetRequestDto;
import com.example.budgetingapp.dtos.user.response.UserLoginResponseDto;
import com.example.budgetingapp.entities.ResetToken;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.repositories.resetpassword.ResetTokenRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final Environment environment;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final JwtAccUtil jwtAccUtil;
    private final PasswordEncoder passwordEncoder;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDto.email(), requestDto.password()));
        String token = jwtAccUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }

    public void initiatePasswordReset(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(
                    "User with email " + email + " was not found");
        }
        JwtAccUtil jwtAccUtil1 = new JwtAccUtil(Objects.requireNonNull(
                environment.getProperty("jwt.secret")));
        JwtRefResUtil jwtRefResUtil = new JwtRefResUtil(jwtAccUtil1);
        jwtRefResUtil.acquireExpiration(RESET, environment);
        ResetToken resetToken = new ResetToken();
        resetToken.setResetToken(jwtAccUtil.generateToken(email));
        resetTokenRepository.save(resetToken);

        emailService.sendSimpleEmail(email, resetToken.getResetToken());
    }

    public UserLoginRequestDto resetPassword(UserResetRequestDto userResetRequestDto) {
        jwtAccUtil.isValidToken(userResetRequestDto.token());
        //TODO проброс ошибки на клиента про устаревший токен
        String login = jwtAccUtil.getUsername(userResetRequestDto.token());
        if (userResetRequestDto.email().equals(login)) {
            //TODO ошибка на клиента про то, что email не совпал
            if (!userResetRequestDto.newPassword()
                    .equals(userResetRequestDto.repeatNewPassword())) {
                //TODO проброс ошибки на клиента
            }
            Optional<User> optionalUser = userRepository.findByEmail(userResetRequestDto.email());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(passwordEncoder.encode(userResetRequestDto.newPassword()));
                userRepository.save(user);
            }
        }
        return new UserLoginRequestDto(userResetRequestDto.email(),
                userResetRequestDto.newPassword());
    }
}
