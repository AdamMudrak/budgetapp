package com.example.budgetingapp.repositories.transactions.transactionsspecs.income;

import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.repositories.specifications.SpecificationBuilder;
import com.example.budgetingapp.repositories.specifications.SpecificationProviderManager;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.TransactionSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IncomeSpecificationBuilder implements SpecificationBuilder<Income> {
    private final SpecificationProviderManager<Income> incomeSpecificationProviderManager;
    private final TransactionSpecificationBuilder<Income> transactionSpecificationBuilder;

    @Override
    public Specification<Income> build(FilterTransactionByDaysDto transactionsDto) {
        Specification<Income> specification = Specification.where(null);
        return transactionSpecificationBuilder.getSpecification(
                specification,
                incomeSpecificationProviderManager,
                transactionsDto);
    }
}
