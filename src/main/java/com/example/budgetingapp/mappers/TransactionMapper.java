package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateRequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.GetResponseTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseTransactionDto;
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
    @Mapping(source = "categoryId", target = "expenseCategory.id")
    @Mapping(target = "transactionDate", ignore = true)
    Expense toExpense(UpdateRequestTransactionDto requestTransactionDto);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "categoryId", target = "incomeCategory.id")
    @Mapping(target = "transactionDate", ignore = true)
    Income toIncome(RequestTransactionDto requestTransactionDto);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "categoryId", target = "incomeCategory.id")
    @Mapping(target = "transactionDate", ignore = true)
    Income toIncome(UpdateRequestTransactionDto requestTransactionDto);

    @AfterMapping
    default void setTransactionDate(@MappingTarget Expense expense,
                                   RequestTransactionDto requestTransactionDto) {
        expense.setTransactionDate(LocalDate.parse(requestTransactionDto.transactionDate()));
    }

    @AfterMapping
    default void setTransactionDate(@MappingTarget Income income,
                                    RequestTransactionDto requestTransactionDto) {
        income.setTransactionDate(LocalDate.parse(requestTransactionDto.transactionDate()));
    }

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "incomeCategory.id", target = "categoryId")
    @Mapping(source = "account.name", target = "accountName")
    @Mapping(source = "incomeCategory.name", target = "categoryName")
    GetResponseTransactionDto toIncomeDto(Income income);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "expenseCategory.id", target = "categoryId")
    @Mapping(source = "account.name", target = "accountName")
    @Mapping(source = "expenseCategory.name", target = "categoryName")
    GetResponseTransactionDto toExpenseDto(Expense expense);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "incomeCategory.id", target = "categoryId")
    SaveAndUpdateResponseTransactionDto toPersistIncomeDto(Income income);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "expenseCategory.id", target = "categoryId")
    SaveAndUpdateResponseTransactionDto toPersistExpenseDto(Expense expense);
}
