package com.example.budgetingapp.repositories.transactions;

import com.example.budgetingapp.entities.transactions.Expense;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT expense FROM Expense expense WHERE expense.account.user.id = :userId")
    List<Expense> findAllByUserId(Long userId, Pageable pageable);

    List<Expense> findAllByAccountId(Long accountId, Pageable pageable);

}
