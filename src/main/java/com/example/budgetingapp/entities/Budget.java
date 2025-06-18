package com.example.budgetingapp.entities;

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
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate fromDate;
    @Column(nullable = false)
    private LocalDate toDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_category_id", nullable = false)
    private ExpenseCategory expenseCategory;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private BigDecimal limitSum;
    @Column(nullable = false)
    private BigDecimal currentSum = BigDecimal.ZERO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isExceeded = false;
}
