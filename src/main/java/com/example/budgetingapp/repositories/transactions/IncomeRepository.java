package com.example.budgetingapp.repositories.transactions;

import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.repositories.TransactionsSorter;
import jakarta.persistence.criteria.Join;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IncomeRepository extends JpaRepository<Income, Long>,
        JpaSpecificationExecutor<Income> {

    Optional<Income> findByIdAndUserId(Long id, Long userId);

    List<Income> findAllByUserId(Long userId);

    default List<Income> findAllByUserIdUnpaged(Long userId,
                                                 Specification<Income> specification) {
        Specification<Income> userIdSpecification = getUserIdSpecification(userId);
        if (specification != null) {
            userIdSpecification = userIdSpecification.and(specification);
        }
        return findAll(userIdSpecification);
    }

    default Page<Income> findAllByUserIdPaged(Long userId,
                                              Specification<Income> specification,
                                              Pageable pageable) {
        Specification<Income> userIdSpecification = getUserIdSpecification(userId);
        if (specification != null) {
            userIdSpecification = userIdSpecification.and(specification);
        }
        return findAll(userIdSpecification,
                TransactionsSorter.getSortOrSortByDefault(pageable));
    }

    private Specification<Income> getUserIdSpecification(Long userId) {
        return ((root, query, criteriaBuilder) -> {
            Join<Income, User> incomeUserJoin = root.join("user");
            return criteriaBuilder.equal(incomeUserJoin.get("id"), userId);
        });
    }
}
