package com.example.budgetingapp.repositories.specifications;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(FilterTransactionsDto filterDto);
}
