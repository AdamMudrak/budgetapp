package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.transactionfieldspecs;

import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import jakarta.persistence.criteria.Join;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategoryIdsSpecificationProvider implements SpecificationProvider<Expense> {
    @Override
    public String getKey() {
        return "categoryIds";
    }

    @Override
    public Specification<Expense> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) -> {
            Join<Expense, ExpenseCategory> expenseCategoryJoin = root.join("expenseCategory");
            return expenseCategoryJoin.get("id").in(Arrays.stream(params).toArray());
        });
    }
}
