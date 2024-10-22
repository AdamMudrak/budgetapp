package com.example.budgetingapp.entities;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.ACCOUNTS;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.BOOLEAN_TO_INT;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = ACCOUNTS)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private BigDecimal balance;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false, columnDefinition = BOOLEAN_TO_INT)
    private boolean byDefault = false;
}