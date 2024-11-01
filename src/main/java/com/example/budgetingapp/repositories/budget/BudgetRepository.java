package com.example.budgetingapp.repositories.budget;

import com.example.budgetingapp.entities.Budget;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    boolean existsByIdAndUserId(Long id, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);

    //TODO Optional<Budget> findByIsTopLevelBudget(boolean isTopLevelBudget);

    List<Budget> findAllByUserId(Long userId);

    @Transactional
    @Modifying
    void deleteByIdAndUserId(Long id, Long userId);
}
