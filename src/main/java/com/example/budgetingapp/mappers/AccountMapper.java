package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.account.request.CreateAccountDto;
import com.example.budgetingapp.dtos.account.response.AccountDto;
import com.example.budgetingapp.entities.Account;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AccountMapper {
    Account toAccount(CreateAccountDto createAccountDto);

    @Mapping(source = "user.id", target = "userId")
    AccountDto toDto(Account account);

    @Mapping(source = "user.id", target = "userId")
    List<AccountDto> toDtoList(List<Account> accounts);
}
