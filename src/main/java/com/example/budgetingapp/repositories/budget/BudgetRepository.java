package com.example.budgetingapp.repositories.budget;

import com.example.budgetingapp.entities.Budget;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Query("SELECT COUNT(*) FROM Budget budget "
            + " WHERE budget.user.id=:userId")
    int countBudgetsByUserId(Long userId);

    @Query("SELECT COUNT(budget) > 0 FROM Budget budget "
            + " JOIN budget.expenseCategories expenseCategories"
            + " WHERE expenseCategories.id=:expenseCategoryId"
            + " AND expenseCategories.user.id=:userId")
    boolean existsByExpenseCategoryIdAndUserId(Long expenseCategoryId, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);

    Optional<Budget> findByIdAndUserId(Long id, Long userId);

    Optional<Budget> findByUserIdAndIsTopLevelBudget(Long userId, boolean isTopLevelBudget);

    List<Budget> findAllByUserId(Long userId);

    List<Budget> findAllByUserIdAndIsTopLevelBudget(Long userId, boolean isTopLevelBudget);

    @Transactional
    @Modifying
    void deleteByIdAndUserId(Long id, Long userId);
}
