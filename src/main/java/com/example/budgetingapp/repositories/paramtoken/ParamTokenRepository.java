package com.example.budgetingapp.repositories.paramtoken;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParamTokenRepository extends
        JpaRepository<com.example.budgetingapp.entities.ParamToken, Long> {
    Optional<com.example.budgetingapp.entities.ParamToken> findByParameter(String parameter);
}
