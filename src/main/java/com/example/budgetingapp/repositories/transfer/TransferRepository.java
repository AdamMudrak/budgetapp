package com.example.budgetingapp.repositories.transfer;

import com.example.budgetingapp.entities.transfers.Transfer;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllByUserId(Long userId, Pageable pageable);
}
