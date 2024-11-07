package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.budgets.request.BudgetRequestDto;
import com.example.budgetingapp.dtos.budgets.response.BudgetResponseDto;
import com.example.budgetingapp.dtos.budgets.response.TopLevelBudgetResponseDto;
import com.example.budgetingapp.entities.Budget;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
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
    default void setCategory(@MappingTarget Budget budget, BudgetRequestDto budgetRequestDto) {
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(budgetRequestDto.categoryId());
        budget.setExpenseCategories(Set.of(expenseCategory));
    }

    @Mapping(target = "categoryId", ignore = true)
    BudgetResponseDto toBudgetDto(Budget budget);

    @AfterMapping
    default void setCategoryId(@MappingTarget BudgetResponseDto budgetResponseDto, Budget budget) {
        budgetResponseDto.setCategoryId(budget.getExpenseCategories().stream()
                .map(ExpenseCategory::getId).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "No category found for budget " + budget.getId())));
    }

    @Mapping(target = "categoryIds", ignore = true)
    TopLevelBudgetResponseDto toTopLevelBudgetDto(Budget budget);

    @AfterMapping
    default void setCategoryIds(@MappingTarget TopLevelBudgetResponseDto budgetResponseDto,
                                Budget budget) {
        Set<Long> categoryIds = budget.getExpenseCategories().stream()
                .map(ExpenseCategory::getId)
                .collect(Collectors.toSet());
        budgetResponseDto.setCategoryIds(categoryIds);
    }
}
