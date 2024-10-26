package com.example.budgetingapp.repositories.transactions.expenses;

import com.example.budgetingapp.entities.transactions.expenses.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
