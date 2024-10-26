package com.example.budgetingapp.repositories.transactions;

import com.example.budgetingapp.entities.transactions.Expense;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findAllByAccountId(Long accountId, Pageable pageable);

}
