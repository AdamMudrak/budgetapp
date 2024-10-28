package com.example.budgetingapp.mappers;

import static com.example.budgetingapp.constants.Constants.DATE_PATTERN;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.entities.transactions.Income;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        expense.setTransactionDate(LocalDate.parse(requestTransactionDto.getTransactionDate(),
                DateTimeFormatter.ofPattern(DATE_PATTERN)));
    }

    @AfterMapping
    default void setTransactionDate(@MappingTarget Income income,
                                    RequestTransactionDto requestTransactionDto) {
        income.setTransactionDate(LocalDate.parse(requestTransactionDto.getTransactionDate(),
                DateTimeFormatter.ofPattern(DATE_PATTERN)));
    }

    ResponseTransactionDto toIncomeDto(Income income);

    ResponseTransactionDto toExpenseDto(Expense expense);

    @AfterMapping
    default void setTransactionDateDto(@MappingTarget RequestTransactionDto requestTransactionDto,
                                       Income income) {
        requestTransactionDto.setTransactionDate(income.getTransactionDate()
                .format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
    }

    @AfterMapping
    default void setTransactionDateDto(@MappingTarget RequestTransactionDto requestTransactionDto,
                                       Expense expense) {
        requestTransactionDto.setTransactionDate(expense.getTransactionDate()
                .format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
    }
}
