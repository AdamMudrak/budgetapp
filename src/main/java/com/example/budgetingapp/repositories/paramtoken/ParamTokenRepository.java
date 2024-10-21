package com.example.budgetingapp.repositories.paramtoken;

import com.example.budgetingapp.entities.ParamToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParamTokenRepository extends JpaRepository<ParamToken, Long> {
    @Query("SELECT paramToken "
            + "FROM ParamToken paramToken "
            + "WHERE paramToken.parameter = :parameter "
            + "AND paramToken.actionToken = :actionToken")
    Optional<ParamToken> findByParameterAndActionToken(String parameter, String actionToken);

    @Query("SELECT paramToken "
            + "FROM ParamToken paramToken "
            + "WHERE paramToken.actionToken = :actionToken")
    Optional<ParamToken> findByActionToken(String actionToken);
}
