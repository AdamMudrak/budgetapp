package com.example.budgetingapp.repositories;

import com.example.budgetingapp.entities.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT COUNT(*) FROM Account account "
            + " WHERE account.user.id=:userId")
    int countAccountsByUserId(Long userId);

    List<Account> findAllByUserId(Long userId);

    boolean existsByUserIdAndCurrency(Long userId, String currency);

    boolean existsByUserIdAndName(Long userId, String name);

    boolean existsByUserIdAndByDefault(Long userId, boolean byDefault);

    boolean existsByIdAndUserId(Long id, Long userId);

    Optional<Account> findByIdAndUserId(Long id, Long userId);

    Optional<Account> findByUserIdAndByDefault(Long userId, boolean byDefault);
}
