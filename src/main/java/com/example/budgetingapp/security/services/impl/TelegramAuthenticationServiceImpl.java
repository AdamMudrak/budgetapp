package com.example.budgetingapp.security.services.impl;

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
import com.example.budgetingapp.security.services.TelegramAuthenticationService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramAuthenticationServiceImpl implements TelegramAuthenticationService {
    private final UserRepository userRepository;
    private final ActionTokenRepository actionTokenRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public TelegramAuthenticationResponseDto register(TelegramAuthenticationRequestDto requestDto) {
        ActionToken actionToken = actionTokenRepository.findByActionToken(requestDto.token())
                .orElseThrow(() ->
                        new EntityNotFoundException("No such request from telegram bot!"));
        actionTokenRepository.delete(actionToken);
        User user = userMapper.toTelegramUser(requestDto);
        assignUserRole(user);
        user.setEnabled(true);
        userRepository.save(user);
        return new TelegramAuthenticationResponseDto(SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM);
    }

    private void assignUserRole(User user) {
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));
    }
}
