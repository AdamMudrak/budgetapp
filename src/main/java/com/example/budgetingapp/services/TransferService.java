package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.transfers.request.TransferRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TransferResponseDto;

public interface TransferService {
    TransferResponseDto transfer(Long userId,
                                 TransferRequestDto requestTransactionDto);
}
