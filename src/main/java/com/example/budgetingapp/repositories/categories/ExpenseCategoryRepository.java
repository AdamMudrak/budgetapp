package com.example.budgetingapp.repositories.categories;

import com.example.budgetingapp.entities.categories.ExpenseCategory;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    boolean existsByNameAndUserId(String name, Long userId);

    List<ExpenseCategory> getAllByUserId(Long userId, Pageable pageable);

    Optional<ExpenseCategory> findByIdAndUserId(Long id, Long userId);

    @Transactional
    @Modifying
    void deleteByIdAndUserId(Long id, Long userId);
}
