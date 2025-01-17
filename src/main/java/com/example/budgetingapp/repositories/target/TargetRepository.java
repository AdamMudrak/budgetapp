package com.example.budgetingapp.repositories.target;

import com.example.budgetingapp.entities.Target;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TargetRepository extends JpaRepository<Target, Long> {
    @Query("SELECT COUNT(*) FROM Target target "
            + " WHERE target.user.id=:userId")
    int countTargetsByUserId(Long userId);

    List<Target> findAllByUserId(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    Optional<Target> findByIdAndUserId(Long id, Long userId);
}
