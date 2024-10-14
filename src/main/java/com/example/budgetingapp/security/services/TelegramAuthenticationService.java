package com.example.budgetingapp.security.services;

import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM;

import com.example.budgetingapp.dtos.user.request.TelegramAuthenticationRequestDto;
import com.example.budgetingapp.dtos.user.response.TelegramAuthenticationResponseDto;
import com.example.budgetingapp.entities.ActionToken;
import com.example.budgetingapp.entities.Role;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.UserMapper;
import com.example.budgetingapp.repositories.actiontoken.ActionTokenRepository;
import com.example.budgetingapp.repositories.role.RoleRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramAuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ActionTokenRepository actionTokenRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public TelegramAuthenticationResponseDto registerOrLogin(
            TelegramAuthenticationRequestDto requestDto) {
        ActionToken actionToken = actionTokenRepository.findByActionToken(requestDto.token())
                .orElseThrow(() ->
                        new EntityNotFoundException("No such request from telegram bot!"));
        actionTokenRepository.delete(actionToken);
        if (!userRepository.existsByUserName(requestDto.userName())) {
            return register(requestDto);
        } else {
            return login(requestDto);
        }
    }

    private TelegramAuthenticationResponseDto register(
            TelegramAuthenticationRequestDto requestDto) {
        User user = userMapper.toTelegramUser(requestDto);
        assignUserRole(user);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        userRepository.save(user);
        return new TelegramAuthenticationResponseDto(SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM);
    }

    private TelegramAuthenticationResponseDto login(TelegramAuthenticationRequestDto requestDto) {
        User user = userRepository.findByUserName(requestDto.userName()).orElseThrow(() ->
                new EntityNotFoundException("User with login "
                        + requestDto.userName() + " was not found"));
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        userRepository.save(user);
        return new TelegramAuthenticationResponseDto(SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM);
    }

    private void assignUserRole(User user) {
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));
    }
}
