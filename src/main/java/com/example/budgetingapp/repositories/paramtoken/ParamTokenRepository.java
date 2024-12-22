package com.example.budgetingapp.repositories.paramtoken;

import com.example.budgetingapp.entities.tokens.ParamToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParamTokenRepository extends JpaRepository<ParamToken, Long> {
    Optional<ParamToken> findByParameterAndActionToken(String parameter, String actionToken);

    Optional<ParamToken> findByActionToken(String actionToken);
}
