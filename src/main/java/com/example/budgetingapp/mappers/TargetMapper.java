package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transfers.request.TargetTransactionRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TargetTransactionResponseDto;
import com.example.budgetingapp.entities.transfers.Target;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface TargetMapper {

    Target toTarget(TargetTransactionRequestDto targetTransactionRequestDto);

    TargetTransactionResponseDto toTargetDto(Target target);
}
