package com.example.budgetingapp.repositories.categories;

import com.example.budgetingapp.entities.categories.IncomeCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    boolean existsByNameAndUserId(String name, Long userId);

    List<IncomeCategory> getAllByUserId(Long userId);

    Optional<IncomeCategory> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
