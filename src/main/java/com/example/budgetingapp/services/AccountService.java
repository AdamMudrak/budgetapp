package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.account.request.CreateAccountDto;
import com.example.budgetingapp.dtos.account.request.SetAccountAsDefaultDto;
import com.example.budgetingapp.dtos.account.request.UpdateAccountDto;
import com.example.budgetingapp.dtos.account.response.AccountDto;
import java.util.List;

public interface AccountService {
    AccountDto saveAccount(Long userId, CreateAccountDto requestDto);

    AccountDto updateAccountName(Long userId, UpdateAccountDto requestDto);

    List<AccountDto> getAllAccountsByUserId(Long userId);

    AccountDto getDefaultAccountByUserId(Long userId);

    AccountDto setAccountByDefault(Long userId, SetAccountAsDefaultDto requestDto);
}
