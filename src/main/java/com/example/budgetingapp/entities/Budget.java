package com.example.budgetingapp.entities;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.BOOLEAN_TO_INT;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.BUDGETS;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.EXPENSE_CATEGORY_ID;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.USER_ID;

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
@Table(name = BUDGETS)
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
    @JoinColumn(name = EXPENSE_CATEGORY_ID, nullable = false)
    private ExpenseCategory expenseCategory;
    @Column(nullable = false)
    private BigDecimal limitSum;
    @Column(nullable = false)
    private BigDecimal currentSum = BigDecimal.ZERO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;
    @Column(nullable = false, columnDefinition = BOOLEAN_TO_INT)
    private boolean isExceeded = false;
}
