package com.example.budgetingapp.repositories.transactions.incomes;

import com.example.budgetingapp.entities.transactions.incomes.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}
