package com.example.budgetingapp.entities;

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
@Table(name = "targets")
public class Target {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal expectedSum;
    @Column(nullable = false)
    private BigDecimal currentSum = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal downPayment;
    @Column(nullable = false)
    private LocalDate achievedBefore;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean achieved = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
