package com.example.budgetingapp.repositories.specifications;

import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(FilterTransactionByDaysDto filterDto);
}
