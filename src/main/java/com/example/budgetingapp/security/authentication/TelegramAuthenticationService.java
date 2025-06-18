package com.example.budgetingapp.security.authentication;

import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM;

import com.example.budgetingapp.dtos.users.request.TelegramAuthenticationRequestDto;
import com.example.budgetingapp.dtos.users.response.TelegramAuthenticationResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.tokens.ActionToken;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.UserMapper;
import com.example.budgetingapp.repositories.ActionTokenRepository;
import com.example.budgetingapp.repositories.UserRepository;
import com.example.budgetingapp.services.utils.AssignDefaultUserObjectsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramAuthenticationService {
    private final AssignDefaultUserObjectsUtil defaultUserObjectsUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ActionTokenRepository actionTokenRepository;
    private final UserMapper userMapper;

    public TelegramAuthenticationResponseDto registerOrGetLogin(
            TelegramAuthenticationRequestDto requestDto) {
        ActionToken actionToken = actionTokenRepository.findByActionToken(requestDto.token())
                .orElseThrow(() ->
                        new EntityNotFoundException("No such request from telegram bot!"));
        actionTokenRepository.delete(actionToken);
        if (!userRepository.existsByUserName(requestDto.userName())) {
            return register(requestDto);
        } else {
            return getLogin(requestDto);
        }
    }

    private TelegramAuthenticationResponseDto register(
            TelegramAuthenticationRequestDto requestDto) {
        User user = userMapper.toTelegramUser(requestDto);
        defaultUserObjectsUtil.assignUserRole(user);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        userRepository.save(user);
        defaultUserObjectsUtil.assignDefaultAccount(user);
        defaultUserObjectsUtil.assignDefaultIncomeCategories(user);
        defaultUserObjectsUtil.assignDefaultExpenseCategories(user);
        return new TelegramAuthenticationResponseDto(SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM);
    }

    private TelegramAuthenticationResponseDto getLogin(
            TelegramAuthenticationRequestDto requestDto) {
        User user = userRepository.findByUserName(requestDto.userName()).orElseThrow(() ->
                new EntityNotFoundException("User with login "
                        + requestDto.userName() + " was not found"));
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        userRepository.save(user);
        return new TelegramAuthenticationResponseDto(SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM);
    }
}
