package com.example.budgetingapp.services.interfaces;

import com.example.budgetingapp.dtos.transfers.request.TransferRequestDto;
import com.example.budgetingapp.dtos.transfers.response.GetTransfersPageDto;
import com.example.budgetingapp.dtos.transfers.response.TransferResponseDto;
import org.springframework.data.domain.Pageable;

public interface TransferService {
    TransferResponseDto transfer(Long userId,
                                 TransferRequestDto requestTransactionDto);

    GetTransfersPageDto getAllTransfersByUserId(Long userId, Pageable pageable);

    void deleteByTransferId(Long userId, Long transferId);
}
