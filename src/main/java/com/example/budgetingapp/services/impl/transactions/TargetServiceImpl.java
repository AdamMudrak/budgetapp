package com.example.budgetingapp.services.impl.transactions;

import com.example.budgetingapp.dtos.transactions.request.TargetTransactionRequestDto;
import com.example.budgetingapp.dtos.transactions.request.TargetTransactionResponseDto;
import com.example.budgetingapp.services.TargetService;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class TargetServiceImpl implements TargetService {
    @Override
    public TargetTransactionResponseDto saveTarget(Long userId, TargetTransactionRequestDto requestTransactionDto) {
        return null;
    }

    @Override
    public List<TargetTransactionResponseDto> getAllTargets(Long userId, Pageable pageable) {
        return List.of();
    }

    @Override
    public void deleteByTargetId(Long userId, Long targetId, Long accountId) {

    }
}
