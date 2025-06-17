package com.example.budgetingapp.services.impl;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;
import static com.example.budgetingapp.constants.security.SecurityConstants.REGISTERED;

import com.example.budgetingapp.dtos.users.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.users.response.UserRegistrationResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.RegistrationException;
import com.example.budgetingapp.mappers.UserMapper;
import com.example.budgetingapp.repositories.UserRepository;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import com.example.budgetingapp.services.UserService;
import com.example.budgetingapp.services.email.RegisterConfirmEmailService;
import com.example.budgetingapp.services.utils.AssignDefaultUserObjectsUtil;
import com.example.budgetingapp.services.utils.ParamFromHttpRequestUtil;
import com.example.budgetingapp.services.utils.RedirectUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final ParamFromHttpRequestUtil requestParamUtil;
    private final RedirectUtil redirectUtil;
    private final AssignDefaultUserObjectsUtil defaultUserObjectsUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtStrategy jwtStrategy;
    private final RegisterConfirmEmailService registerConfirmEmailService;
    @Value("${registration.confirmation.link}")
    private String happyRedirectPath;
    @Value("${action.error.link}")
    private String actionErrorPath;

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByUserName(requestDto.userName())) {
            throw new RegistrationException("User with email "
                    + requestDto.userName() + " already exists");
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        defaultUserObjectsUtil.assignUserRole(user);
        userRepository.save(user);

        defaultUserObjectsUtil.assignDefaultAccount(user);
        defaultUserObjectsUtil.assignDefaultExpenseCategories(user);
        defaultUserObjectsUtil.assignDefaultIncomeCategories(user);
        registerConfirmEmailService.sendRegisterConfirmEmail(user.getUsername());
        return new UserRegistrationResponseDto(REGISTERED);
    }

    @Override
    public ResponseEntity<Void> confirmRegistration(HttpServletRequest httpServletRequest) {
        String token = requestParamUtil.parseRandomParameterAndToken(httpServletRequest);
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACTION);
        try {
            jwtAbstractUtil.isValidToken(token);
        } catch (Exception e) {
            redirectUtil.redirect(actionErrorPath);
        }
        String email = jwtAbstractUtil.getUsername(token);
        User user = userRepository.findByUserName(email).orElseThrow(
                () -> new EntityNotFoundException("User with email "
                        + email + " was not found"));
        user.setEnabled(true);
        userRepository.save(user);

        return redirectUtil.redirect(happyRedirectPath);
    }
}
