package com.example.budgetingapp.repositories.actiontoken;

import com.example.budgetingapp.entities.ActionToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActionTokenRepository extends JpaRepository<ActionToken, Long> {
    @Query("SELECT actionToken "
            + "FROM ActionToken actionToken "
            + "WHERE actionToken.actionToken = :actionToken")
    Optional<ActionToken> findByActionToken(String actionToken);
}
