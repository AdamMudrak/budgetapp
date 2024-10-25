package com.example.budgetingapp.services.impl;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_ACCOUNT_CURRENCY;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_ACCOUNT_NAME;
import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONFIRMATION;
import static com.example.budgetingapp.constants.security.SecurityConstants.REGISTERED;
import static com.example.budgetingapp.constants.security.SecurityConstants.REGISTERED_AND_CONFIRMED;

import com.example.budgetingapp.dtos.user.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.user.response.UserRegistrationResponseDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.tokens.ParamToken;
import com.example.budgetingapp.entities.Role;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.RegistrationException;
import com.example.budgetingapp.mappers.UserMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.paramtoken.ParamTokenRepository;
import com.example.budgetingapp.repositories.role.RoleRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import com.example.budgetingapp.services.UserService;
import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ParamTokenRepository paramTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtStrategy jwtStrategy;
    private final PasswordEmailService passwordEmailService;

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByUserName(requestDto.userName())) {
            throw new RegistrationException("User with email "
                    + requestDto.userName() + " already exists");
        }
        User user = userMapper.toUser(requestDto);
        assignUserRole(user);
        assignDefaultAccount(user);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        userRepository.save(user);

        passwordEmailService.sendActionMessage(user.getUsername(), CONFIRMATION);
        return new UserRegistrationResponseDto(REGISTERED);
    }

    @Override
    public UserRegistrationResponseDto confirmRegistration(String token) {
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
        return new UserRegistrationResponseDto(REGISTERED_AND_CONFIRMED);
    }

    private void assignUserRole(User user) {
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));
    }

    private void assignDefaultAccount(User user) {
        Account account = new Account();
        account.setName(DEFAULT_ACCOUNT_NAME);
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(DEFAULT_ACCOUNT_CURRENCY);
        account.setByDefault(true);
        accountRepository.save(account);
    }
}
