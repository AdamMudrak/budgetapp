package com.example.budgetingapp.repositories.account;

import com.example.budgetingapp.entities.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT COUNT(*) FROM Account account "
            + " WHERE account.user.id=:userId")
    int countAccountsByUserId(Long userId);

    List<Account> getAllByUserId(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    boolean existsByUserIdAndByDefault(Long userId, boolean byDefault);

    Optional<Account> findByIdAndUserId(Long id, Long userId);

    Optional<Account> findByUserIdAndByDefault(Long userId, boolean byDefault);
}
