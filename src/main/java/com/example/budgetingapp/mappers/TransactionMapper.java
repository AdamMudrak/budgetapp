package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transactions.request.CreateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.GetTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseDto;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.entities.transactions.Income;
import java.time.LocalDate;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface TransactionMapper {
    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "categoryId", target = "expenseCategory.id")
    @Mapping(target = "transactionDate", ignore = true)
    Expense toExpense(CreateTransactionDto requestTransactionDto);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "categoryId", target = "expenseCategory.id")
    @Mapping(target = "transactionDate", ignore = true)
    Expense toExpense(UpdateTransactionDto requestTransactionDto);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "categoryId", target = "incomeCategory.id")
    @Mapping(target = "transactionDate", ignore = true)
    Income toIncome(CreateTransactionDto requestTransactionDto);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "categoryId", target = "incomeCategory.id")
    @Mapping(target = "transactionDate", ignore = true)
    Income toIncome(UpdateTransactionDto requestTransactionDto);

    @AfterMapping
    default void setTransactionDate(@MappingTarget Expense expense,
                                   CreateTransactionDto requestTransactionDto) {
        expense.setTransactionDate(LocalDate.parse(requestTransactionDto.transactionDate()));
    }

    @AfterMapping
    default void setTransactionDate(@MappingTarget Income income,
                                    CreateTransactionDto requestTransactionDto) {
        income.setTransactionDate(LocalDate.parse(requestTransactionDto.transactionDate()));
    }

    List<GetTransactionDto> toIncomeDtoList(List<Income> incomes);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "incomeCategory.id", target = "categoryId")
    @Mapping(source = "account.name", target = "accountName")
    @Mapping(source = "incomeCategory.name", target = "categoryName")
    GetTransactionDto toIncomeDto(Income income);

    List<GetTransactionDto> toExpenseDtoList(List<Expense> expenses);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "expenseCategory.id", target = "categoryId")
    @Mapping(source = "account.name", target = "accountName")
    @Mapping(source = "expenseCategory.name", target = "categoryName")
    GetTransactionDto toExpenseDto(Expense expense);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "incomeCategory.id", target = "categoryId")
    SaveAndUpdateResponseDto toPersistIncomeDto(Income income);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "expenseCategory.id", target = "categoryId")
    SaveAndUpdateResponseDto toPersistExpenseDto(Expense expense);
}
