package com.example.budgetingapp.repositories.resetpassword;

import com.example.budgetingapp.entities.ResetToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByResetToken(String resetToken);
}
