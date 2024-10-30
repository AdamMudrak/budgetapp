package com.example.budgetingapp.controllers.transactions;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.Constants.TRANSACTION_PAGEABLE_EXAMPLE;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.ADD_INCOME;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.ADD_INCOME_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.DELETE_INCOME_BY_ID;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.DELETE_INCOME_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.GET_ALL_INCOMES;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.GET_ALL_INCOMES_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.INCOME;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.INCOME_TRANSACTIONS;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_ADDED_INCOME;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_DELETED_INCOME;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_RETRIEVED_INCOMES;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_UPDATED_INCOME;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.TRANSACTION_API_NAME;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.UPDATE_INCOME_BY_ID;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.UPDATE_INCOME_SUMMARY;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RestController;

@Validated
@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = TRANSACTION_API_NAME)
@RequestMapping(INCOME_TRANSACTIONS)
public class IncomeTransactionController {
    private final TransactionService incomeTransactionService;

    public IncomeTransactionController(@Qualifier(INCOME)
                                       TransactionService incomeTransactionService) {
        this.incomeTransactionService = incomeTransactionService;
    }

    @Operation(summary = ADD_INCOME_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_ADDED_INCOME)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(ADD_INCOME)
    public ResponseTransactionDto addIncomeTransaction(@AuthenticationPrincipal User user,
            @RequestBody RequestTransactionDto requestTransactionDto) {
        return incomeTransactionService.saveTransaction(user.getId(), requestTransactionDto);
    }

    @Operation(summary = GET_ALL_INCOMES_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_INCOMES)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping(GET_ALL_INCOMES)
    public List<ResponseTransactionDto> getAllIncomesTransactions(
            @AuthenticationPrincipal User user,
            FilterTransactionsDto filterTransactionsDto,
            @Parameter(example = TRANSACTION_PAGEABLE_EXAMPLE) Pageable pageable) {
        return incomeTransactionService.getAllTransactions(user.getId(),
                filterTransactionsDto, pageable);
    }

    @Operation(summary = UPDATE_INCOME_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_UPDATED_INCOME)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PutMapping(UPDATE_INCOME_BY_ID)
    public ResponseTransactionDto updateIncomeTransaction(@AuthenticationPrincipal User user,
            @RequestBody RequestTransactionDto requestTransactionDto,
            @PathVariable Long transactionId) {
        return incomeTransactionService.updateTransaction(user.getId(),
                requestTransactionDto, transactionId);
    }

    @Operation(summary = DELETE_INCOME_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_DELETED_INCOME)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @DeleteMapping(DELETE_INCOME_BY_ID)
    public void deleteIncomeById(@AuthenticationPrincipal User user,
                                 @PathVariable @Positive Long transactionId) {
        incomeTransactionService.deleteByTransactionId(user.getId(), transactionId);
    }
}
