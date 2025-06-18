package com.example.budgetingapp.repositories.transactions.transactionsspecs;

import com.example.budgetingapp.entities.Account;
import jakarta.persistence.criteria.Join;
import java.time.LocalDate;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TransactionSpecificationProvider<T,C> {
    public Specification<T> getAccountIdSpecification(String[] params) {
        Long accountId = Long.parseLong(params[0]);
        return ((root, query, criteriaBuilder) -> {
            Join<T, Account> accountJoin = root.join("account");
            return criteriaBuilder.equal(accountJoin.get("id"), accountId);
        });
    }

    public Specification<T> getCategoryIdsSpecification(String[] params, String categoryType) {
        return ((root, query, criteriaBuilder) -> {
            Join<T, C> categoryJoin = root.join(categoryType);
            return categoryJoin.get("id").in(Arrays.stream(params).toArray());
        });
    }

    public Specification<T> getFromDateSpecification(String[] params) {
        LocalDate fromDate = LocalDate.parse(params[0]);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), fromDate));
    }

    public Specification<T> getToDateSpecification(String[] params) {
        LocalDate toDate = LocalDate.parse(params[0]);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), toDate));
    }
}
