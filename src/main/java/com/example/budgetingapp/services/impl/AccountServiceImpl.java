package com.example.budgetingapp.services.impl;

import com.example.budgetingapp.dtos.account.request.CreateAccountDto;
import com.example.budgetingapp.dtos.account.request.SetAccountAsDefaultDto;
import com.example.budgetingapp.dtos.account.request.UpdateAccountDto;
import com.example.budgetingapp.dtos.account.response.AccountDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.AlreadyExistsException;
import com.example.budgetingapp.exceptions.ConflictException;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.AccountMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.AccountService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountDto saveAccount(Long userId, CreateAccountDto requestDto) {
        if (accountRepository.existsByUserIdAndName(userId, requestDto.name())) {
            throw new AlreadyExistsException("You already have account named "
                    + requestDto.name());
        }
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        Account newAccount = accountMapper.toAccount(requestDto);
        newAccount.setUser(currentUser);
        if (!accountRepository.existsByUserIdAndByDefault(userId, true)) {
            newAccount.setByDefault(true);
        }
        return accountMapper.toDto(accountRepository.save(newAccount));
    }

    @Override
    public AccountDto updateAccountName(Long userId, UpdateAccountDto requestDto) {
        if (accountRepository.existsByUserIdAndName(userId, requestDto.newName())) {
            throw new AlreadyExistsException("You already have account named "
                    + requestDto.newName());
        }
        Account account = accountRepository.findByUserIdAndAccountName(userId,
                        requestDto.currentName()).orElseThrow(
                                () -> new EntityNotFoundException("No account with name "
                        + requestDto.currentName() + " was found"));
        return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    public List<AccountDto> getAllAccountsByUserId(Long userId) {
        return accountMapper
                .toDtoList(accountRepository
                        .getAllByUserId(userId));
    }

    @Override
    public AccountDto getDefaultAccountByUserId(Long userId) {
        return accountMapper
                .toDto(accountRepository
                        .findByUserIdAndByDefault(userId,true)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No default account for user with id " + userId + " was found")));
    }

    @Override
    public AccountDto setAccountByDefault(Long userId, SetAccountAsDefaultDto requestDto) {
        Account account = accountRepository.findByUserIdAndAccountName(userId, requestDto.name())
                .orElseThrow(() -> new EntityNotFoundException("No account with name "
                        + requestDto.name() + " was found"));

        Optional<Account> byDefault = accountRepository.findByUserIdAndByDefault(userId,true);
        if (byDefault.isPresent()) {
            Account presentDefaultAccount = byDefault.get();
            if (presentDefaultAccount.getName().equals(requestDto.name())) {
                throw new ConflictException("Account "
                        + requestDto.name() + "is already set by default");
            }
            presentDefaultAccount.setByDefault(false);
            accountRepository.save(presentDefaultAccount);
        }
        account.setByDefault(true);
        return accountMapper.toDto(accountRepository.save(account));
    }
}
