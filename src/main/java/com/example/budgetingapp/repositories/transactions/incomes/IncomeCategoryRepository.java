package com.example.budgetingapp.repositories.transactions.incomes;

import com.example.budgetingapp.entities.transactions.incomes.IncomeCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    boolean existsByName(String name);

    List<IncomeCategory> getAllByUserId(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    Optional<IncomeCategory> findByUserIdAndName(Long userId, String name);

    void deleteByUserIdAndName(Long userId, String name);
}
