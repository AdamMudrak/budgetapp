package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.transactionfieldspecs;

import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;

public class CategoryIdsSpecificationProvider implements SpecificationProvider<Expense> {
    @Override
    public String getKey() {
        return "categoryIds";
    }

    @Override
    public Specification<Expense> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) -> root.get("categoryId").in(Arrays
                .stream(params)
                .toArray()));
    }
}
