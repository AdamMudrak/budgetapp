package com.example.budgetingapp.controllers.transactions;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_201;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.Constants.TRANSACTION_PAGEABLE_EXAMPLE;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ACCOUNT_API_NAME;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.ADD_TRANSFER;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.ADD_TRANSFER_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.GET_ALL_TRANSFERS;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.GET_ALL_TRANSFERS_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_ADDED_TRANSFER;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.SUCCESSFULLY_RETRIEVED_TRANSFERS;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.TRANSFERS;

import com.example.budgetingapp.dtos.transfers.request.TransferRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TransferResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = ACCOUNT_API_NAME)
@RequestMapping(TRANSFERS)
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @Operation(summary = ADD_TRANSFER_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description = SUCCESSFULLY_ADDED_TRANSFER)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(ADD_TRANSFER)
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponseDto addTransfer(@AuthenticationPrincipal User user,
                                           @RequestBody TransferRequestDto requestDto) {
        return transferService.transfer(user.getId(), requestDto);
    }

    @Operation(summary = GET_ALL_TRANSFERS_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_RETRIEVED_TRANSFERS)
    @GetMapping(GET_ALL_TRANSFERS)
    public List<TransferResponseDto> getAllTransfers(@AuthenticationPrincipal User user,
                             @Parameter(example = TRANSACTION_PAGEABLE_EXAMPLE) Pageable pageable) {
        return transferService.getAllTransfersByUserId(user.getId(), pageable);
    }
}
