package com.example.budgetingapp.entities;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.BUDGETS;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.USER_ID;

import com.example.budgetingapp.entities.categories.ExpenseCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
    @ManyToMany
    @JoinTable(
            name = "budgets_expense_categories",
            joinColumns = @JoinColumn(name = "budget_id"),
            inverseJoinColumns = @JoinColumn(name = "expense_category_id"))
    private Set<ExpenseCategory> expenseCategories = new HashSet<>();
    @Column(nullable = false)
    private BigDecimal limit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;
}
