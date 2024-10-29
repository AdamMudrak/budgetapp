package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.transactionfieldspecs;

import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
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
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("expense.account.id"), accountId));
    }
}
