package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transactions.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseCategoryDto;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.categories.IncomeCategory;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    IncomeCategory toIncomeCategory(CreateCategoryDto createCategoryDto);

    @Mapping(source = "user.id", target = "userId")
    ResponseCategoryDto toIncomeCategoryDto(IncomeCategory incomeCategory);

    @Mapping(source = "user.id", target = "userId")
    ResponseCategoryDto toExpenseCategoryDto(ExpenseCategory expenseCategory);

    @Mapping(source = "user.id", target = "userId")
    List<ResponseCategoryDto> toIncomeCategoryDtoList(List<IncomeCategory> accounts);

    @Mapping(source = "user.id", target = "userId")
    List<ResponseCategoryDto> toExpenseCategoryDtoList(List<ExpenseCategory> accounts);
}
