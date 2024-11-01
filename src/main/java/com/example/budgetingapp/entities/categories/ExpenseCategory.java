package com.example.budgetingapp.entities.categories;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.EXPENSE_CATEGORIES;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table (name = EXPENSE_CATEGORIES)
public class ExpenseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;

    public ExpenseCategory(Long id) {
        this.id = id;
    }
}
