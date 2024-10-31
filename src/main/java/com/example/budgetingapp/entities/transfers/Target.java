package com.example.budgetingapp.entities.transfers;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.BOOLEAN_TO_INT;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.TARGETS;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.USER_ID;

import com.example.budgetingapp.entities.User;
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
@Table(name = TARGETS)
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
    private BigDecimal monthlyDownPayment;
    @Column(nullable = false)
    private LocalDate achievedBefore;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false, columnDefinition = BOOLEAN_TO_INT)
    private boolean achieved = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;
}
