package com.example.budgetingapp.services.implementations;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.ACCOUNT_QUANTITY_THRESHOLD;

import com.example.budgetingapp.dtos.accounts.request.CreateAccountDto;
import com.example.budgetingapp.dtos.accounts.request.UpdateAccountDto;
import com.example.budgetingapp.dtos.accounts.response.AccountDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.conflictexpections.AlreadyExistsException;
import com.example.budgetingapp.exceptions.conflictexpections.ConflictException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.AccountMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.interfaces.AccountService;
import jakarta.transaction.Transactional;
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
        if (accountRepository.countAccountsByUserId(userId) >= ACCOUNT_QUANTITY_THRESHOLD) {
            throw new ConflictException("You can't have more than " + ACCOUNT_QUANTITY_THRESHOLD
             + " accounts!");
        }
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        if (accountRepository.existsByUserIdAndName(userId, requestDto.name())) {
            throw new AlreadyExistsException("You already have account named "
                    + requestDto.name());
        }
        Account newAccount = accountMapper.toAccount(requestDto);
        newAccount.setUser(currentUser);
        if (!accountRepository.existsByUserIdAndByDefault(userId, true)) {
            newAccount.setByDefault(true);
        }
        return accountMapper.toDto(accountRepository.save(newAccount));
    }

    @Override
    public AccountDto updateAccountName(Long userId, Long accountId, UpdateAccountDto requestDto) {
        if (accountRepository.existsByUserIdAndName(userId, requestDto.newName())) {
            throw new AlreadyExistsException("You already have account named "
                    + requestDto.newName());
        }
        Account account = accountRepository.findByIdAndUserId(accountId, userId).orElseThrow(
                                () -> new EntityNotFoundException("No account with id "
                        + accountId + " was found for user with id " + userId));
        account.setName(requestDto.newName());
        return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    public List<AccountDto> getAllAccountsByUserId(Long userId) {
        return accountMapper
                .toDtoList(accountRepository
                        .findAllByUserId(userId));
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
    public AccountDto getAccountByIdAndUserId(Long userId, Long accountId) {
        return accountMapper
                .toDto(accountRepository
                        .findByIdAndUserId(accountId,userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No account with id "
                                        + accountId + " was found for user with id " + userId)));
    }

    @Transactional
    @Override
    public AccountDto setAccountByDefault(Long userId, Long accountId) {
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("No account with id "
                        + accountId + " was found for user with id " + userId));

        Optional<Account> byDefault = accountRepository.findByUserIdAndByDefault(userId,true);
        if (byDefault.isPresent()) {
            Account presentDefaultAccount = byDefault.get();
            if (presentDefaultAccount.getId().equals(accountId)) {
                throw new ConflictException("Account with id "
                        + accountId + " is already set by default");
            }
            presentDefaultAccount.setByDefault(false);
            accountRepository.save(presentDefaultAccount);
        }
        account.setByDefault(true);
        return accountMapper.toDto(accountRepository.save(account));
    }
}
