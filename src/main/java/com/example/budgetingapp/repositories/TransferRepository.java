package com.example.budgetingapp.repositories;

import com.example.budgetingapp.entities.Transfer;
import com.example.budgetingapp.entities.User;
import jakarta.persistence.criteria.Join;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransferRepository extends JpaRepository<Transfer, Long>,
        JpaSpecificationExecutor<Transfer> {

    Optional<Transfer> findByIdAndUserId(Long id, Long userId);

    default Page<Transfer> findAllByUserIdPaged(Long userId, Pageable pageable) {
        return findAll(getUserIdSpecification(userId),
                TransactionsSorter.getSortOrSortByDefault(pageable));
    }

    private Specification<Transfer> getUserIdSpecification(Long userId) {
        return (((root, query, criteriaBuilder) -> {
            Join<Transfer, User> transferUserJoin = root.join("user");
            return criteriaBuilder.equal(transferUserJoin.get("id"), userId);
        }));
    }
}
