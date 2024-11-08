package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budgets.response.BudgetResponseDto;
import com.example.budgetingapp.entities.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BudgetMapper {
    @Mapping(source = "categoryId", target = "expenseCategory.id")
    Budget toBudget(BudgetRequestDto budgetRequestDto);

    @Mapping(source = "expenseCategory.id", target = "categoryId")
    BudgetResponseDto toBudgetDto(Budget budget);
}
