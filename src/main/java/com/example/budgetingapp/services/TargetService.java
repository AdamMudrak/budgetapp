package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.transactions.request.TargetTransactionRequestDto;
import com.example.budgetingapp.dtos.transactions.request.TargetTransactionResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TargetService {
    TargetTransactionResponseDto saveTarget(Long userId,
                                            TargetTransactionRequestDto requestTransactionDto);

    List<TargetTransactionResponseDto> getAllTargets(Long userId,
                                                    Pageable pageable);

    void deleteByTargetId(Long userId, Long targetId, Long accountId);
}
