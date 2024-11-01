package com.example.budgetingapp.security.services;

import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_ACCOUNT_CURRENCY;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_ACCOUNT_NAME;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_EXPENSE_CATEGORIES_LIST;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_INCOME_CATEGORIES_LIST;

import com.example.budgetingapp.dtos.users.request.TelegramAuthenticationRequestDto;
import com.example.budgetingapp.dtos.users.response.TelegramAuthenticationResponseDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.Role;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.categories.IncomeCategory;
import com.example.budgetingapp.entities.tokens.ActionToken;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.UserMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.actiontoken.ActionTokenRepository;
import com.example.budgetingapp.repositories.categories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.categories.IncomeCategoryRepository;
import com.example.budgetingapp.repositories.role.RoleRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramAuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
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
        assignDefaultAccount(user);
        assignDefaultIncomeCategories(user);
        assignDefaultExpenseCategories(user);
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

    private void assignDefaultAccount(User user) {
        Account account = new Account();
        account.setName(DEFAULT_ACCOUNT_NAME);
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(DEFAULT_ACCOUNT_CURRENCY);
        account.setByDefault(true);
        accountRepository.save(account);
    }

    private void assignDefaultExpenseCategories(User user) {
        for (String categoryName : DEFAULT_EXPENSE_CATEGORIES_LIST) {
            ExpenseCategory expenseCategory = new ExpenseCategory();
            expenseCategory.setName(categoryName);
            expenseCategory.setUser(user);
            expenseCategoryRepository.save(expenseCategory);
        }
    }

    private void assignDefaultIncomeCategories(User user) {
        for (String categoryName : DEFAULT_INCOME_CATEGORIES_LIST) {
            IncomeCategory incomeCategory = new IncomeCategory();
            incomeCategory.setName(categoryName);
            incomeCategory.setUser(user);
            incomeCategoryRepository.save(incomeCategory);
        }
    }
}
