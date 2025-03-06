package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.users.request.TelegramAuthenticationRequestDto;
import com.example.budgetingapp.dtos.users.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.users.request.userlogindtos.InnerUserLoginDto;
import com.example.budgetingapp.dtos.users.request.userlogindtos.UserEmailLoginDto;
import com.example.budgetingapp.dtos.users.request.userlogindtos.UserTelegramLoginDto;
import com.example.budgetingapp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(UserRegistrationRequestDto userRegistrationRequestDto);

    User toTelegramUser(TelegramAuthenticationRequestDto authenticationRequestDto);

    @Mapping(source = "phoneNumber", target = "userName")
    InnerUserLoginDto toInnerUserDto(
            UserTelegramLoginDto userTelegramLoginRequestDto);

    @Mapping(source = "email", target = "userName")
    InnerUserLoginDto toInnerUserDto(UserEmailLoginDto userEmailLoginRequestDto);
}
