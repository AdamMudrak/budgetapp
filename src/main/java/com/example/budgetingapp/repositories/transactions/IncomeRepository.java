package com.example.budgetingapp.repositories.transactions;

import com.example.budgetingapp.entities.transactions.Income;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IncomeRepository extends JpaRepository<Income, Long>,
        JpaSpecificationExecutor<Income> {

    List<Income> findAllByUserId(Long userId, Pageable pageable);

    List<Income> findAllByAccountId(Long accountId, Pageable pageable);
}
