package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transfers.request.TargetTransactionRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TargetTransactionResponseDto;
import com.example.budgetingapp.entities.transfers.Target;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface TargetMapper {

    Target toTarget(TargetTransactionRequestDto targetTransactionRequestDto);

    @Mapping(target = "monthLeft", ignore = true)
    TargetTransactionResponseDto toTargetDto(Target target);

    @AfterMapping
    default void setMonthLeft(@MappingTarget
                              TargetTransactionResponseDto targetTransactionResponseDto,
                              Target target) {
        Long monthLeft = ChronoUnit.MONTHS.between(LocalDate.now(), target.getAchievedBefore());
        targetTransactionResponseDto.setMonthLeft(monthLeft);
    }
}
