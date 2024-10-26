package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.entities.transactions.Income;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface TransactionMapper {

    Expense toExpense(RequestTransactionDto requestTransactionDto);

    Income toIncome(RequestTransactionDto requestTransactionDto);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "incomeCategory.id", target = "categoryId")
    ResponseTransactionDto toIncomeDto(Income income);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "expenseCategory.id", target = "categoryId")
    ResponseTransactionDto toExpenseDto(Expense expense);
}
