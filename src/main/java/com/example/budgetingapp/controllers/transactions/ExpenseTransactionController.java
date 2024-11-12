package com.example.budgetingapp.controllers.transactions;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_201;
import static com.example.budgetingapp.constants.Constants.CODE_204;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.Constants.TRANSACTION_PAGEABLE_EXAMPLE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.ADD_EXPENSE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.ADD_EXPENSE_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.DELETE_EXPENSE_BY_ID;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.DELETE_EXPENSE_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.EXPENSE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.EXPENSE_TRANSACTIONS;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.GET_ALL_EXPENSES;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.GET_ALL_EXPENSES_FOR_CHARTS_DAYS;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.GET_ALL_EXPENSES_FOR_CHARTS_MONTHS_YEARS;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY_DAY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.GET_ALL_EXPENSES_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_ADDED_EXPENSE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_DELETED_EXPENSE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_RETRIEVED_EXPENSES;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS_DAY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.SUCCESSFULLY_UPDATED_EXPENSE;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.UPDATE_EXPENSE_BY_ID;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.UPDATE_EXPENSE_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.TransactionsCommonConstants.TRANSACTION_API_NAME;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByMonthOrYear;
import com.example.budgetingapp.dtos.transactions.response.AccumulatedResultDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.TransactionService;
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
@RequestMapping(EXPENSE_TRANSACTIONS)
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
    @PostMapping(ADD_EXPENSE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseTransactionDto addExpenseTransaction(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody RequestTransactionDto requestTransactionDto) {
        return expenseTransactionService.saveTransaction(user.getId(), requestTransactionDto);
    }

    @Operation(summary = GET_ALL_EXPENSES_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_EXPENSES)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping(GET_ALL_EXPENSES)
    public List<ResponseTransactionDto> getAllExpenseTransactions(
            @AuthenticationPrincipal User user,
            @Valid FilterTransactionsDto filterTransactionsDto,
            @Parameter(example = TRANSACTION_PAGEABLE_EXAMPLE) Pageable pageable) {
        return expenseTransactionService.getAllTransactions(user.getId(),
                filterTransactionsDto, pageable);
    }

    @Operation(summary = GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY_DAY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS_DAY)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping(GET_ALL_EXPENSES_FOR_CHARTS_DAYS)
    public List<AccumulatedResultDto> getExpensesForDaysCharts(
            @AuthenticationPrincipal User user,
            @Valid FilterTransactionsDto filterTransactionsDto) {
        return expenseTransactionService
                .getSumOfTransactionsForPeriodOfTime(
                        user.getId(), filterTransactionsDto);
    }

    @Operation(summary = GET_ALL_EXPENSES_FOR_CHARTS_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_EXPENSES_FOR_CHARTS)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping(GET_ALL_EXPENSES_FOR_CHARTS_MONTHS_YEARS)
    public List<AccumulatedResultDto> getExpensesForMonthOrYearCharts(
            @AuthenticationPrincipal User user,
            @Valid ChartTransactionRequestDtoByMonthOrYear
                    chartTransactionRequestDtoByMonthOrYear) {
        return expenseTransactionService
                .getSumOfTransactionsForMonthOrYear(
                        user.getId(), chartTransactionRequestDtoByMonthOrYear);
    }

    @Operation(summary = UPDATE_EXPENSE_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_UPDATED_EXPENSE)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PutMapping(UPDATE_EXPENSE_BY_ID)
    public ResponseTransactionDto updateExpenseTransaction(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody RequestTransactionDto requestTransactionDto,
            @PathVariable @Positive Long transactionId) {
        return expenseTransactionService.updateTransaction(
                user.getId(), requestTransactionDto, transactionId);
    }

    @Operation(summary = DELETE_EXPENSE_SUMMARY)
    @ApiResponse(responseCode = CODE_204, description =
            SUCCESSFULLY_DELETED_EXPENSE)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @DeleteMapping(DELETE_EXPENSE_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpenseById(@AuthenticationPrincipal User user,
                                  @PathVariable @Positive Long transactionId) {
        expenseTransactionService.deleteByTransactionId(user.getId(), transactionId);
    }
}
