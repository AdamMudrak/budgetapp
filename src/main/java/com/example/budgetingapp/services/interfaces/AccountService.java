package com.example.budgetingapp.services.interfaces;

import com.example.budgetingapp.dtos.accounts.request.CreateAccountDto;
import com.example.budgetingapp.dtos.accounts.request.UpdateAccountDto;
import com.example.budgetingapp.dtos.accounts.response.AccountDto;
import java.util.List;

public interface AccountService {
    AccountDto saveAccount(Long userId, CreateAccountDto requestDto);

    AccountDto updateAccountName(Long userId, Long accountId, UpdateAccountDto requestDto);

    List<AccountDto> getAllAccountsByUserId(Long userId);

    AccountDto getDefaultAccountByUserId(Long userId);

    AccountDto getAccountByIdAndUserId(Long userId, Long accountId);

    AccountDto setAccountByDefault(Long userId, Long accountId);
}
