package com.example.budgetingapp.repositories;

import com.example.budgetingapp.entities.categories.IncomeCategory;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    @Query("SELECT COUNT(*) FROM IncomeCategory incomeCategory "
            + " WHERE incomeCategory.user.id=:userId")
    int countCategoriesByUserId(Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);

    List<IncomeCategory> findAllByUserId(Long userId);

    Optional<IncomeCategory> findByIdAndUserId(Long id, Long userId);

    Optional<IncomeCategory> findByNameAndUserId(String name, Long userId);

    @Transactional
    @Modifying
    void deleteByIdAndUserId(Long id, Long userId);
}
