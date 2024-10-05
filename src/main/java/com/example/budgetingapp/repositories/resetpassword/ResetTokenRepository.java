package com.example.budgetingapp.repositories.resetpassword;

import com.example.budgetingapp.entities.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
}
