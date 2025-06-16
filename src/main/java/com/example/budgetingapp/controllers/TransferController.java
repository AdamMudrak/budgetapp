package com.example.budgetingapp.controllers;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_201;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.Constants.TRANSACTION_PAGEABLE_EXAMPLE;
import static com.example.budgetingapp.constants.controllers.TransferControllerConstants.ADD_TRANSFER_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransferControllerConstants.DELETE_TRANSFER_BY_ID_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransferControllerConstants.GET_ALL_TRANSFERS_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransferControllerConstants.SUCCESSFULLY_ADDED_TRANSFER;
import static com.example.budgetingapp.constants.controllers.TransferControllerConstants.SUCCESSFULLY_DELETED_TRANSFER;
import static com.example.budgetingapp.constants.controllers.TransferControllerConstants.SUCCESSFULLY_RETRIEVED_TRANSFERS;
import static com.example.budgetingapp.constants.controllers.TransferControllerConstants.TRANSFERS_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.TransferControllerConstants.TRANSFERS_API_NAME;

import com.example.budgetingapp.dtos.transfers.request.TransferRequestDto;
import com.example.budgetingapp.dtos.transfers.response.GetTransfersPageDto;
import com.example.budgetingapp.dtos.transfers.response.TransferResponseDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = TRANSFERS_API_NAME, description = TRANSFERS_API_DESCRIPTION)
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @Operation(summary = ADD_TRANSFER_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description = SUCCESSFULLY_ADDED_TRANSFER)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping("/add-transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponseDto addTransfer(@AuthenticationPrincipal User user,
                                           @Valid @RequestBody TransferRequestDto requestDto) {
        return transferService.transfer(user.getId(), requestDto);
    }

    @Operation(summary = GET_ALL_TRANSFERS_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_RETRIEVED_TRANSFERS)
    @GetMapping("/get-all-transfers")
    public GetTransfersPageDto getAllTransfers(@AuthenticationPrincipal User user,
                           @Parameter(example = TRANSACTION_PAGEABLE_EXAMPLE) Pageable pageable) {
        return transferService.getAllTransfersByUserId(user.getId(), pageable);
    }

    @Operation(summary = DELETE_TRANSFER_BY_ID_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_DELETED_TRANSFER)
    @DeleteMapping("/delete-transfer/{transferId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudget(@AuthenticationPrincipal User user,
                             @PathVariable Long transferId) {
        transferService.deleteByTransferId(user.getId(), transferId);
    }
}
