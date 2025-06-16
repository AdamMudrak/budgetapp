package com.example.budgetingapp.repositories;

import com.example.budgetingapp.entities.tokens.ActionToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionTokenRepository extends JpaRepository<ActionToken, Long> {
    Optional<ActionToken> findByActionToken(String actionToken);
}
