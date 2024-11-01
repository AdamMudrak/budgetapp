package com.example.budgetingapp.repositories.budget;

import com.example.budgetingapp.entities.Budget;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    boolean existsByIdAndUserId(Long id, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);

    Optional<Budget> findByUserIdAndIsTopLevelBudget(Long userId, boolean isTopLevelBudget);

    List<Budget> findAllByUserId(Long userId);

    List<Budget> findAllByUserIdAndIsTopLevelBudget(Long userId, boolean isTopLevelBudget);

    @Transactional
    @Modifying
    void deleteByIdAndUserId(Long id, Long userId);
}
