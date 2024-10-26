package com.example.budgetingapp.entities.transactions;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.ACCOUNT_ID;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.EXPENSES;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.EXPENSE_CATEGORY_ID;

import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = EXPENSES)
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = ACCOUNT_ID, nullable = false)
    private Account account;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate expenseDate;
    @ManyToOne
    @JoinColumn(name = EXPENSE_CATEGORY_ID, nullable = false)
    private ExpenseCategory expenseCategory;
}
