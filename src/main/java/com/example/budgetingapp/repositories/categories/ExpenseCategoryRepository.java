package com.example.budgetingapp.repositories.categories;

import com.example.budgetingapp.entities.categories.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
}
