package com.example.budgetingapp.mappers;

import static com.example.budgetingapp.constants.Constants.MONTHS_IN_A_YEAR;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.targets.request.CreateTargetDto;
import com.example.budgetingapp.dtos.targets.response.TargetDto;
import com.example.budgetingapp.entities.Target;
import java.time.LocalDate;
import java.time.Period;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface TargetMapper {
    @Mapping(target = "achievedBefore", ignore = true)
    Target toTarget(CreateTargetDto targetTransactionRequestDto);

    @Mapping(target = "periodLeft", ignore = true)
    TargetDto toTargetDto(Target target);

    @AfterMapping
    default void setMonthLeft(@MappingTarget
                                  TargetDto targetTransactionResponseDto,
                              Target target) {
        Period period = Period.between(LocalDate.now(), target.getAchievedBefore());
        int months = period.getMonths() + (period.getYears() * MONTHS_IN_A_YEAR);
        int days = period.getDays();
        String monthOrMonths = (months == 1) ? " month " : " months ";
        String dayOrDays = (days == 1) ? " day " : " days ";
        String paidMonthlyOrDaily = (months == 0) ? " Paid daily. " : "Paid monthly. ";
        targetTransactionResponseDto.setPeriodLeft(paidMonthlyOrDaily + months + monthOrMonths
                + "and " + days + dayOrDays + "left");
    }

    @AfterMapping
    default void setAchievedBefore(@MappingTarget Target target,
                                   CreateTargetDto targetTransactionRequestDto) {
        target.setAchievedBefore(LocalDate.parse(targetTransactionRequestDto.achievedBefore()));
    }
}
