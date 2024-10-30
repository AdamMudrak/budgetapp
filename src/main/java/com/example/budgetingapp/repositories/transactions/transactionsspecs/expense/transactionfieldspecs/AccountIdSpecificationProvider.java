package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.transactionfieldspecs;

import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AccountIdSpecificationProvider implements SpecificationProvider<Expense> {
    @Override
    public String getKey() {
        return "accountId";
    }

    @Override
    public Specification<Expense> getSpecification(String[] params) {
        Long accountId = Long.parseLong(params[0]);
        return ((root, query, criteriaBuilder) -> {
            Join<Expense, Account> expenseAccountJoin = root.join("account");
            return criteriaBuilder.equal(expenseAccountJoin.get("id"), accountId);
        });
    }
}
