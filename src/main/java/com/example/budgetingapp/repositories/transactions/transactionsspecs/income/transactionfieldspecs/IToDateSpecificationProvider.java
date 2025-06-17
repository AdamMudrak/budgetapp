package com.example.budgetingapp.repositories.transactions.transactionsspecs.income.transactionfieldspecs;

import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.TransactionSpecificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IToDateSpecificationProvider implements SpecificationProvider<Income> {
    private final TransactionSpecificationProvider<Income> transactionSpecificationProvider;

    @Override
    public String getKey() {
        return "toDate";
    }

    @Override
    public Specification<Income> getSpecification(String[] params) {
        return transactionSpecificationProvider.getToDateSpecification(params);
    }
}
