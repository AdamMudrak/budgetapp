package com.example.budgetingapp.repositories.transactions.transactionsspecs;

import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.transactions.Expense;
import jakarta.persistence.criteria.Join;
import java.time.LocalDate;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TransactionSpecificationProvider<T> {
    public Specification<T> getAccountIdSpecification(String[] params) {
        Long accountId = Long.parseLong(params[0]);
        return ((root, query, criteriaBuilder) -> {
            Join<Expense, Account> expenseAccountJoin = root.join("account");
            return criteriaBuilder.equal(expenseAccountJoin.get("id"), accountId);
        });
    }

    public Specification<T> getCategoryIdsSpecification(String[] params) {
        return ((root, query, criteriaBuilder) -> {
            Join<Expense, ExpenseCategory> expenseCategoryJoin = root.join("expenseCategory");
            return expenseCategoryJoin.get("id").in(Arrays.stream(params).toArray());
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
