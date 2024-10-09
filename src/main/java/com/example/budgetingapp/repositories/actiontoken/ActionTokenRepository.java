package com.example.budgetingapp.repositories.actiontoken;

import com.example.budgetingapp.entities.ActionToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionTokenRepository extends JpaRepository<ActionToken, Long> {
    Optional<ActionToken> findByActionToken(String actionToken);
}
