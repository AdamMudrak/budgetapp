package com.example.budgetingapp.controllers.transactions;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_201;
import static com.example.budgetingapp.constants.Constants.CODE_204;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.Constants.TRANSACTION_PAGEABLE_EXAMPLE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.ADD_EXPENSE_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.DELETE_EXPENSE_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.EXPENSE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY_DAY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.GET_ALL_EXPENSES_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_ADDED_EXPENSE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_DELETED_EXPENSE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_RETRIEVED_EXPENSES;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS_DAY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_UPDATED_EXPENSE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.UPDATE_EXPENSE_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.TransactionsCommonConstants.TRANSACTION_API_NAME;

import com.example.budgetingapp.dtos.transactions.request.CreateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByMonthsYearsDto;
import com.example.budgetingapp.dtos.transactions.response.GetTransactionsPageDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseDto;
import com.example.budgetingapp.dtos.transactions.response.charts.SumsByPeriodDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.interfaces.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = TRANSACTION_API_NAME)
@RequestMapping("/expense-transactions")
public class ExpenseTransactionController {
    private final TransactionService expenseTransactionService;

    public ExpenseTransactionController(@Qualifier(EXPENSE)
                                        TransactionService expenseTransactionService) {
        this.expenseTransactionService = expenseTransactionService;
    }

    @Operation(summary = ADD_EXPENSE_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description =
            SUCCESSFULLY_ADDED_EXPENSE)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping("/add-expense")
    @ResponseStatus(HttpStatus.CREATED)
    public SaveAndUpdateResponseDto addExpenseTransaction(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateTransactionDto requestTransactionDto) {
        return expenseTransactionService.saveTransaction(user.getId(), requestTransactionDto);
    }

    @Operation(summary = GET_ALL_EXPENSES_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_EXPENSES)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping("/get-all-expenses")
    public GetTransactionsPageDto getAllExpenseTransactions(
            @AuthenticationPrincipal User user,
            @Valid FilterTransactionByDaysDto filterTransactionsDto,
            @Parameter(example = TRANSACTION_PAGEABLE_EXAMPLE) Pageable pageable) {
        return expenseTransactionService.getAllTransactions(user.getId(),
                filterTransactionsDto, pageable);
    }

    @Operation(summary = GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY_DAY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS_DAY)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping("/get-all-expenses-for-charts-days")
    public List<SumsByPeriodDto> getExpensesForDaysCharts(
            @AuthenticationPrincipal User user,
            @Valid FilterTransactionByDaysDto filterTransactionsDto) {
        return expenseTransactionService
                .getSumOfTransactionsForPeriodOfTime(
                        user.getId(), filterTransactionsDto);
    }

    @Operation(summary = GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping("/get-all-expenses-for-charts-months-years")
    public List<SumsByPeriodDto> getExpensesForMonthOrYearCharts(
            @AuthenticationPrincipal User user,
            @Valid FilterTransactionByMonthsYearsDto
                    chartTransactionRequestDtoByMonthOrYear) {
        return expenseTransactionService
                .getSumOfTransactionsForMonthOrYear(
                        user.getId(), chartTransactionRequestDtoByMonthOrYear);
    }

    @Operation(summary = UPDATE_EXPENSE_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_UPDATED_EXPENSE)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PutMapping("/update-expense/{transactionId}")
    public SaveAndUpdateResponseDto updateExpenseTransaction(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateTransactionDto requestTransactionDto,
            @PathVariable @Positive Long transactionId) {
        return expenseTransactionService.updateTransaction(
                user.getId(), requestTransactionDto, transactionId);
    }

    @Operation(summary = DELETE_EXPENSE_SUMMARY)
    @ApiResponse(responseCode = CODE_204, description =
            SUCCESSFULLY_DELETED_EXPENSE)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @DeleteMapping("/delete-expense/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpenseById(@AuthenticationPrincipal User user,
                                  @PathVariable @Positive Long transactionId) {
        expenseTransactionService.deleteByTransactionId(user.getId(), transactionId);
    }
}
