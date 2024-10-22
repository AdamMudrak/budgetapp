package com.example.budgetingapp.repositories.account;

import com.example.budgetingapp.entities.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> getAllByUserId(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    Optional<Account> findByUserIdAndName(Long userId, String name);

    Optional<Account> findByUserIdAndByDefault(Long userId, boolean byDefault);
}