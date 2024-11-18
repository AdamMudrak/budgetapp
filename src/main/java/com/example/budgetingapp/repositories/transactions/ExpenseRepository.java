package com.example.budgetingapp.repositories.transactions;

import com.example.budgetingapp.entities.transactions.Expense;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExpenseRepository extends JpaRepository<Expense, Long>,
        JpaSpecificationExecutor<Expense> {

    Optional<Expense> findByIdAndUserId(Long id, Long userId);

    List<Expense> findAllByUserId(Long userId);

    default List<Expense> findAllByUserIdUnpaged(Long userId,
                                  Specification<Expense> specification) {
        Specification<Expense> userIdSpecification = getUserIdSpecification(userId);
        if (specification != null) {
            userIdSpecification = userIdSpecification.and(specification);
        }
        return findAll(userIdSpecification);
    }

    default List<Expense> findAllByUserIdPaged(Long userId,
                                  Specification<Expense> specification,
                                    Pageable pageable) {
        Specification<Expense> userIdSpecification = getUserIdSpecification(userId);
        if (specification != null) {
            userIdSpecification = userIdSpecification.and(specification);
        }
        return findAll(userIdSpecification, pageable).getContent();
    }

    private Specification<Expense> getUserIdSpecification(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }
}
