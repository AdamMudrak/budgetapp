package com.example.budgetingapp.controllers.transactions;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_201;
import static com.example.budgetingapp.constants.Constants.CODE_204;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.Constants.TRANSACTION_PAGEABLE_EXAMPLE;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.ADD_INCOME_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.DELETE_INCOME_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.GET_ALL_INCOMES_FOR_CHARTS_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.GET_ALL_INCOMES_FOR_CHARTS_SUMMARY_DAY;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.GET_ALL_INCOMES_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.INCOME;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.SUCCESSFULLY_ADDED_INCOME;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.SUCCESSFULLY_DELETED_INCOME;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.SUCCESSFULLY_RETRIEVED_INCOMES;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.SUCCESSFULLY_RETRIEVED_INCOMES_FOR_CHARTS;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.SUCCESSFULLY_RETRIEVED_INCOMES_FOR_CHARTS_DAY;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.SUCCESSFULLY_UPDATED_INCOME;
import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.UPDATE_INCOME_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.TransactionsCommonConstants.TRANSACTION_API_NAME;

import com.example.budgetingapp.dtos.transactions.request.CreateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByMonthsYearsDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterType;
import com.example.budgetingapp.dtos.transactions.response.GetTransactionsPageDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseDto;
import com.example.budgetingapp.dtos.transactions.response.charts.SumsByPeriodDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = TRANSACTION_API_NAME)
@RequestMapping("/income-transactions")
public class IncomeTransactionController {
    private final TransactionService incomeTransactionService;

    public IncomeTransactionController(@Qualifier(INCOME)
                                       TransactionService incomeTransactionService) {
        this.incomeTransactionService = incomeTransactionService;
    }

    @Operation(summary = ADD_INCOME_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description =
            SUCCESSFULLY_ADDED_INCOME)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping("/add-income")
    @ResponseStatus(HttpStatus.CREATED)
    public SaveAndUpdateResponseDto addIncomeTransaction(
                                @AuthenticationPrincipal User user,
                                @Valid @RequestBody CreateTransactionDto requestTransactionDto) {
        return incomeTransactionService.saveTransaction(user.getId(), requestTransactionDto);
    }

    @Operation(summary = GET_ALL_INCOMES_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_INCOMES)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping("/get-all-incomes")
    public GetTransactionsPageDto getAllIncomesTransactions(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Set<Long> categoryIds,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @Parameter(example = TRANSACTION_PAGEABLE_EXAMPLE) Pageable pageable) {
        return incomeTransactionService.getAllTransactions(user.getId(),
                new FilterTransactionByDaysDto(accountId,categoryIds,fromDate,toDate), pageable);
    }

    @Operation(summary = GET_ALL_INCOMES_FOR_CHARTS_SUMMARY_DAY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_INCOMES_FOR_CHARTS_DAY)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping("/get-all-incomes-for-charts-days")
    public List<SumsByPeriodDto> getIncomesForDaysCharts(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Set<Long> categoryIds,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate) {
        return incomeTransactionService
                .getSumOfTransactionsForPeriodOfTime(user.getId(),
                        new FilterTransactionByDaysDto(accountId,categoryIds,fromDate,toDate));
    }

    @Operation(summary = GET_ALL_INCOMES_FOR_CHARTS_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_INCOMES_FOR_CHARTS)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping("/get-all-incomes-for-charts-months-years")
    public List<SumsByPeriodDto> getIncomesForMonthOrYearCharts(
            @AuthenticationPrincipal User user,
            @RequestParam Long accountId,
            @RequestParam(required = false) Set<Long> categoryIds,
            @RequestParam FilterType filterType) {
        return incomeTransactionService
                .getSumOfTransactionsForMonthOrYear(
                        user.getId(),
                        new FilterTransactionByMonthsYearsDto(accountId,categoryIds,filterType));
    }

    @Operation(summary = UPDATE_INCOME_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_UPDATED_INCOME)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PutMapping("/update-income/{transactionId}")
    public SaveAndUpdateResponseDto updateIncomeTransaction(
                             @AuthenticationPrincipal User user,
                             @Valid @RequestBody UpdateTransactionDto requestTransactionDto,
                             @PathVariable Long transactionId) {
        return incomeTransactionService.updateTransaction(user.getId(),
                requestTransactionDto, transactionId);
    }

    @Operation(summary = DELETE_INCOME_SUMMARY)
    @ApiResponse(responseCode = CODE_204, description =
            SUCCESSFULLY_DELETED_INCOME)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @DeleteMapping("/delete-income/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIncomeById(@AuthenticationPrincipal User user,
                                 @PathVariable @Positive Long transactionId) {
        incomeTransactionService.deleteByTransactionId(user.getId(), transactionId);
    }
}
