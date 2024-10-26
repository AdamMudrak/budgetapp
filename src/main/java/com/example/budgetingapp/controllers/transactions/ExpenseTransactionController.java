package com.example.budgetingapp.controllers.transactions;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.ADD_EXPENSE;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.ADD_EXPENSE_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.DELETE_EXPENSE_BY_ID;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.DELETE_EXPENSE_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.EXPENSE;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.EXPENSE_TRANSACTION_API_NAME;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.GET_ALL_EXPENSES;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.GET_ALL_EXPENSES_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_ADDED_EXPENSE;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_DELETED_EXPENSE;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_RETRIEVED_EXPENSES;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_UPDATED_EXPENSE;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.TRANSACTIONS;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.TRANSACTION_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.UPDATE_EXPENSE_BY_ID;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.UPDATE_EXPENSE_SUMMARY;

import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import com.example.budgetingapp.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = EXPENSE_TRANSACTION_API_NAME,
        description = TRANSACTION_API_DESCRIPTION)
@RequestMapping(TRANSACTIONS)
public class ExpenseTransactionController {
    private final TransactionService expenseTransactionService;

    public ExpenseTransactionController(@Qualifier(EXPENSE)
                                        TransactionService expenseTransactionService) {
        this.expenseTransactionService = expenseTransactionService;
    }

    @Operation(summary = ADD_EXPENSE_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_ADDED_EXPENSE)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(ADD_EXPENSE)
    public ResponseTransactionDto addExpenseTransaction(
            @RequestBody RequestTransactionDto requestTransactionDto) {
        return expenseTransactionService.saveTransaction(requestTransactionDto);
    }

    @Operation(summary = GET_ALL_EXPENSES_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_EXPENSES)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping(GET_ALL_EXPENSES)
    public List<ResponseTransactionDto> getAllExpenseTransactions(@PathVariable Long accountId,
                                                                  Pageable pageable) {
        return expenseTransactionService.getAllTransactions(accountId, pageable);
    }

    @Operation(summary = UPDATE_EXPENSE_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_UPDATED_EXPENSE)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PutMapping(UPDATE_EXPENSE_BY_ID)
    public ResponseTransactionDto updateExpenseTransaction(
            @RequestBody RequestTransactionDto requestTransactionDto,
            @PathVariable Long transactionId) {
        return expenseTransactionService.updateTransaction(requestTransactionDto, transactionId);
    }

    @Operation(summary = DELETE_EXPENSE_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_DELETED_EXPENSE)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @DeleteMapping(DELETE_EXPENSE_BY_ID)
    public void deleteExpenseById(@PathVariable Long transactionId,
                                  @PathVariable Long accountId) {
        expenseTransactionService.deleteByTransactionId(transactionId, accountId);
    }
}
