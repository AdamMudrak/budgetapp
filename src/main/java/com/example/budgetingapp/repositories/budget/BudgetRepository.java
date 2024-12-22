package com.example.budgetingapp.repositories.budget;

import com.example.budgetingapp.entities.Budget;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Query("SELECT COUNT(*) FROM Budget budget "
            + " WHERE budget.user.id=:userId")
    int countBudgetsByUserId(Long userId);

    @Query("SELECT COUNT(budget) > 0 FROM Budget budget "
            + " WHERE budget.expenseCategory.id=:expenseCategoryId"
            + " AND budget.expenseCategory.user.id=:userId")
    boolean existsByExpenseCategoryIdAndUserId(Long expenseCategoryId, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    List<Budget> findAllByUserId(Long userId);

    @Transactional
    @Modifying
    void deleteByIdAndUserId(Long id, Long userId);
}
