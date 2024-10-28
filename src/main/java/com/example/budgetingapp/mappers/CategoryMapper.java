package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transactions.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseCategoryDto;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.categories.IncomeCategory;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    IncomeCategory toIncomeCategory(CreateCategoryDto createCategoryDto);

    ExpenseCategory toExpenseCategory(CreateCategoryDto createCategoryDto);

    ResponseCategoryDto toIncomeCategoryDto(IncomeCategory incomeCategory);

    ResponseCategoryDto toExpenseCategoryDto(ExpenseCategory expenseCategory);

    List<ResponseCategoryDto> toIncomeCategoryDtoList(List<IncomeCategory> accounts);

    List<ResponseCategoryDto> toExpenseCategoryDtoList(List<ExpenseCategory> accounts);
}
