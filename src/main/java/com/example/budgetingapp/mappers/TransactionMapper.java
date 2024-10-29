package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.entities.transactions.Income;
import java.time.LocalDate;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface TransactionMapper {

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "categoryId", target = "expenseCategory.id")
    @Mapping(target = "transactionDate", ignore = true)
    Expense toExpense(RequestTransactionDto requestTransactionDto);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "categoryId", target = "incomeCategory.id")
    @Mapping(target = "transactionDate", ignore = true)
    Income toIncome(RequestTransactionDto requestTransactionDto);

    @AfterMapping
    default void setTransactionDate(@MappingTarget Expense expense,
                                   RequestTransactionDto requestTransactionDto) {
        expense.setTransactionDate(LocalDate.parse(requestTransactionDto.getTransactionDate()));
    }

    @AfterMapping
    default void setTransactionDate(@MappingTarget Income income,
                                    RequestTransactionDto requestTransactionDto) {
        income.setTransactionDate(LocalDate.parse(requestTransactionDto.getTransactionDate()));
    }

    ResponseTransactionDto toIncomeDto(Income income);

    ResponseTransactionDto toExpenseDto(Expense expense);
}
