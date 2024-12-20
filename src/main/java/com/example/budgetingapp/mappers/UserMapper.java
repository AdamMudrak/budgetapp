package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.users.request.TelegramAuthenticationRequestDto;
import com.example.budgetingapp.dtos.users.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.users.request.userloginrequestdtos.InnerUserLoginRequestDto;
import com.example.budgetingapp.dtos.users.request.userloginrequestdtos.UserEmailLoginRequestDto;
import com.example.budgetingapp.dtos.users.request.userloginrequestdtos.UserTelegramLoginRequestDto;
import com.example.budgetingapp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(UserRegistrationRequestDto userRegistrationRequestDto);

    User toTelegramUser(TelegramAuthenticationRequestDto authenticationRequestDto);

    @Mapping(source = "phone", target = "userName")
    InnerUserLoginRequestDto toInnerUserDto(
            UserTelegramLoginRequestDto userTelegramLoginRequestDto);

    @Mapping(source = "email", target = "userName")
    InnerUserLoginRequestDto toInnerUserDto(UserEmailLoginRequestDto userEmailLoginRequestDto);
}
