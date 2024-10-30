package com.example.budgetingapp.entities;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.FROM_ACCOUNT_ID;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.TO_ACCOUNT_ID;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.TRANSFERS;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.USER_ID;

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
@Table(name = TRANSFERS)
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate transactionDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FROM_ACCOUNT_ID, nullable = false)
    private Account fromAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TO_ACCOUNT_ID, nullable = false)
    private Account toAccount;
}
