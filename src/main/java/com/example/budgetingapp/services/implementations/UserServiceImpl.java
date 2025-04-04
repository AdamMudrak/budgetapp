package com.example.budgetingapp.services.implementations;

import static com.example.budgetingapp.constants.redirects.RedirectConstants.REGISTRATION_CONFIRMATION_LINK;
import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRMATION;
import static com.example.budgetingapp.constants.security.SecurityConstants.REGISTERED;

import com.example.budgetingapp.dtos.users.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.users.response.UserRegistrationResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.tokens.ParamToken;
import com.example.budgetingapp.exceptions.badrequest.RegistrationException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.UserMapper;
import com.example.budgetingapp.repositories.paramtoken.ParamTokenRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import com.example.budgetingapp.security.utils.AssignDefaultUserObjectsUtil;
import com.example.budgetingapp.services.RedirectUtil;
import com.example.budgetingapp.services.email.PasswordEmailService;
import com.example.budgetingapp.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RedirectUtil redirectUtil;
    private final AssignDefaultUserObjectsUtil defaultUserObjectsUtil;
    private final UserRepository userRepository;
    private final ParamTokenRepository paramTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtStrategy jwtStrategy;
    private final PasswordEmailService passwordEmailService;
    @Value(REGISTRATION_CONFIRMATION_LINK)
    private String redirectPath;

    @Transactional
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
        passwordEmailService.sendActionMessage(user.getUsername(), CONFIRMATION);
        return new UserRegistrationResponseDto(REGISTERED);
    }

    @Transactional
    @Override
    public ResponseEntity<Void> confirmRegistration(String token) {
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACTION);
        String email = jwtAbstractUtil.getUsername(token);
        User user = userRepository.findByUserName(email).orElseThrow(
                () -> new EntityNotFoundException("User with email "
                        + email + " was not found"));
        user.setEnabled(true);
        userRepository.save(user);
        ParamToken paramToken = paramTokenRepository.findByActionToken(token).orElseThrow(()
                -> new EntityNotFoundException("No such request"));
        paramTokenRepository.deleteById(paramToken.getId());

        return redirectUtil.redirect(redirectPath);
    }
}
