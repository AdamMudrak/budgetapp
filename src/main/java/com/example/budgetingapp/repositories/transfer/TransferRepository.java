package com.example.budgetingapp.repositories.transfer;

import com.example.budgetingapp.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
