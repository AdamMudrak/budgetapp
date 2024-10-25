package com.example.budgetingapp.entities.transactions.incomes;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.ACCOUNT_ID;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.INCOMES;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.INCOME_CATEGORY_ID;

import com.example.budgetingapp.entities.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = INCOMES)
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ACCOUNT_ID, nullable = false)
    private Account account;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate incomeDate;
    @ManyToOne
    @JoinColumn(name = INCOME_CATEGORY_ID, nullable = false)
    private IncomeCategory incomeCategory;
}
