package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.categories.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.categories.response.CategoryDto;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.categories.IncomeCategory;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    IncomeCategory toIncomeCategory(CreateCategoryDto createCategoryDto);

    ExpenseCategory toExpenseCategory(CreateCategoryDto createCategoryDto);

    CategoryDto toIncomeCategoryDto(IncomeCategory incomeCategory);

    CategoryDto toExpenseCategoryDto(ExpenseCategory expenseCategory);

    List<CategoryDto> toIncomeCategoryDtoList(List<IncomeCategory> accounts);

    List<CategoryDto> toExpenseCategoryDtoList(List<ExpenseCategory> accounts);
}
