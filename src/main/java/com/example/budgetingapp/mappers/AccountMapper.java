package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.accounts.request.CreateAccountDto;
import com.example.budgetingapp.dtos.accounts.response.AccountDto;
import com.example.budgetingapp.entities.Account;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AccountMapper {
    Account toAccount(CreateAccountDto createAccountDto);

    AccountDto toDto(Account account);

    List<AccountDto> toDtoList(List<Account> accounts);
}
