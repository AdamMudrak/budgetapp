package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.transactionfieldspecs;

import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class ToDateSpecificationProvider implements SpecificationProvider<Expense> {
    @Override
    public String getKey() {
        return "toDate";
    }

    @Override
    public Specification<Expense> getSpecification(String[] params) {
        LocalDate toDate = LocalDate.parse(params[0]);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), toDate));
    }
}
