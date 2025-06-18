package com.example.budgetingapp.repositories;

import com.example.budgetingapp.entities.tokens.ActionToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ActionTokenRepository extends JpaRepository<ActionToken, Long> {
    Optional<ActionToken> findByActionToken(String actionToken);

    boolean existsByActionToken(String actionToken);

    @Transactional
    @Modifying
    void deleteByActionToken(String actionToken);
}
