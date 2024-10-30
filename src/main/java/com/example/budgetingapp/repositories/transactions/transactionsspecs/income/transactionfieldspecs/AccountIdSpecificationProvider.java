package com.example.budgetingapp.repositories.transactions.transactionsspecs.income.transactionfieldspecs;

import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AccountIdSpecificationProvider implements SpecificationProvider<Income> {
    @Override
    public String getKey() {
        return "accountId";
    }

    @Override
    public Specification<Income> getSpecification(String[] params) {
        Long accountId = Long.parseLong(params[0]);
        return ((root, query, criteriaBuilder) -> {
            Join<Income, Account> incomeAccountJoin = root.join("account");
            return criteriaBuilder.equal(incomeAccountJoin.get("id"), accountId);
        });
    }
}
