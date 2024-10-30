package com.example.budgetingapp.repositories.transactions.transactionsspecs.income.transactionfieldspecs;

import com.example.budgetingapp.entities.categories.IncomeCategory;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import jakarta.persistence.criteria.Join;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ICategoryIdsSpecificationProvider implements SpecificationProvider<Income> {
    @Override
    public String getKey() {
        return "categoryIds";
    }

    @Override
    public Specification<Income> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) -> {
            Join<Income, IncomeCategory> incomeCategoryJoin = root.join("incomeCategory");
            return incomeCategoryJoin.get("id").in(Arrays.stream(params).toArray());
        });
    }
}
