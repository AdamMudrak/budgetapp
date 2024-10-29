package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.transfers.request.TransferRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TransferResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TransferService {
    TransferResponseDto transfer(Long userId,
                                 TransferRequestDto requestTransactionDto);

    List<TransferResponseDto> getAllTransfersByUserId(Long userId, Pageable pageable);
}
