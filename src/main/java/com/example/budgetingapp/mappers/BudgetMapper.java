package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.budget.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budget.response.BudgetResponseDto;
import com.example.budgetingapp.entities.Budget;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BudgetMapper {
    @Mapping(target = "expenseCategories", ignore = true)
    Budget toBudget(BudgetRequestDto budgetRequestDto);

    @AfterMapping
    default void setCategories(@MappingTarget Budget budget, BudgetRequestDto budgetRequestDto) {
        Set<ExpenseCategory> categories = budgetRequestDto.categoryIds().stream()
                .map(ExpenseCategory::new)
                .collect(Collectors.toSet());
        budget.setExpenseCategories(categories);
    }

    @Mapping(target = "categoryIds", ignore = true)
    BudgetResponseDto toBudgetDto(Budget budget);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BudgetResponseDto budgetResponseDto, Budget budget) {
        Set<Long> categoryIds = budget.getExpenseCategories().stream()
                .map(ExpenseCategory::getId)
                .collect(Collectors.toSet());
        budgetResponseDto.setCategoryIds(categoryIds);
    }
}
