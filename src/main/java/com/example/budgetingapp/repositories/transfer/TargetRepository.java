package com.example.budgetingapp.repositories.transfer;

import com.example.budgetingapp.entities.transfers.Target;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetRepository extends JpaRepository<Target, Long> {
    List<Target> findAllByUserId(Long userId, Pageable pageable);

    Optional<Target> findByIdAndUserId(Long id, Long userId);
}
