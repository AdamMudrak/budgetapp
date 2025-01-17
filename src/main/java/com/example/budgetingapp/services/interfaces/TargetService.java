package com.example.budgetingapp.services.interfaces;

import com.example.budgetingapp.dtos.targets.request.CreateTargetDto;
import com.example.budgetingapp.dtos.targets.request.DeleteTargetDto;
import com.example.budgetingapp.dtos.targets.request.ReplenishTargetDto;
import com.example.budgetingapp.dtos.targets.response.TargetDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TargetService {
    TargetDto saveTarget(Long userId,
                         CreateTargetDto requestTransactionDto);

    TargetDto replenishTarget(Long userId,
                              ReplenishTargetDto replenishTargetRequestDto);

    List<TargetDto> getAllTargets(Long userId,
                                  Pageable pageable);

    void deleteByTargetId(Long userId, DeleteTargetDto deleteTargetRequestDto);
}
