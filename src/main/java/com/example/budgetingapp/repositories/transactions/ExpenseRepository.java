package com.example.budgetingapp.repositories.transactions;

import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Expense;
import jakarta.persistence.criteria.Join;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
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

    default Page<Expense> findAllByUserIdPaged(Long userId,
                                               Specification<Expense> specification,
                                               Pageable pageable) {
        Specification<Expense> userIdSpecification = getUserIdSpecification(userId);
        if (specification != null) {
            userIdSpecification = userIdSpecification.and(specification);
        }
        return findAll(userIdSpecification,
                TransactionsSorter.getSortOrSortByDefault(pageable));
    }

    private Specification<Expense> getUserIdSpecification(Long userId) {
        return ((root, query, criteriaBuilder) -> {
            Join<Expense, User> expenseUserJoin = root.join("user");
            return criteriaBuilder.equal(expenseUserJoin.get("id"), userId);
        });
    }
}
