package com.example.budgetingapp.repositories.transactions.expenses;

import com.example.budgetingapp.entities.transactions.expenses.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
}
