package com.example.budgetingapp.services.impl;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRM_REGISTRATION_BODY;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRM_REGISTRATION_SUBJECT;

import com.example.budgetingapp.dtos.user.request.UserRegistrationRequestDto;
import com.example.budgetingapp.entities.ActionToken;
import com.example.budgetingapp.entities.Role;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.RegistrationException;
import com.example.budgetingapp.mappers.UserMapper;
import com.example.budgetingapp.repositories.actiontoken.ActionTokenRepository;
import com.example.budgetingapp.repositories.role.RoleRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.security.EmailService;
import com.example.budgetingapp.security.jwtutils.JwtStrategy;
import com.example.budgetingapp.security.jwtutils.abstraction.JwtAbstractUtil;
import com.example.budgetingapp.services.UserService;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ActionTokenRepository actionTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtStrategy jwtStrategy;
    private final EmailService emailService;

    @Override
    public String register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User with email "
                    + requestDto.email() + " already exists");
        }
        User user = userMapper.toUser(requestDto);
        assignUserRole(user);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        userRepository.save(user);

        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACTION);
        String token = jwtAbstractUtil.generateToken(user.getUsername());
        ActionToken actionToken = new ActionToken();
        actionToken.setActionToken(token);
        actionTokenRepository.save(actionToken);
        sendConfirmationEmail(user.getEmail(), actionToken.getActionToken());
        return "User is registered successfully. "
                + "Check your email to confirm registration. "
                + "Your account will not be available until then";
    }

    @Override
    public String confirmRegistration(String token) {
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACTION);
        String email = jwtAbstractUtil.getUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email "
                        + email + " was not found"));
        user.setEnabled(true);
        userRepository.save(user);
        Optional<ActionToken> actionToken = actionTokenRepository.findByActionToken(token);
        if (actionToken.isEmpty()) {
            throw new EntityNotFoundException("No registration request was found by this link");
        }
        actionTokenRepository.deleteById(actionToken.get().getId());
        return "Your registration is successfully confirmed";
    }

    private void assignUserRole(User user) {
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));
    }

    private void sendConfirmationEmail(String email, String token) {
        emailService.setSubject(CONFIRM_REGISTRATION_SUBJECT);
        emailService.setBody(CONFIRM_REGISTRATION_BODY);
        emailService.sendSimpleEmail(email,
                emailService.formTextForAction(token));
    }
}
