package com.example.budgetingapp.entities.tokens;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.PARAM_TOKENS;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = PARAM_TOKENS)
public class ParamToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String parameter;
    @Column(nullable = false)
    private String actionToken;
}
