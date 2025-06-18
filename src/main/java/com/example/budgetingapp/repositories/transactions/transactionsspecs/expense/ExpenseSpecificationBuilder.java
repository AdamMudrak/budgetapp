package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense;

import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.repositories.specifications.SpecificationBuilder;
import com.example.budgetingapp.repositories.specifications.SpecificationProviderManager;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.TransactionSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExpenseSpecificationBuilder implements SpecificationBuilder<Expense> {
    private final SpecificationProviderManager<Expense> expenseSpecificationProviderManager;
    private final TransactionSpecificationBuilder<Expense> transactionSpecificationBuilder;

    @Override
    public Specification<Expense> build(FilterTransactionByDaysDto transactionsDto) {
        Specification<Expense> specification = Specification.where(null);
        return transactionSpecificationBuilder.getSpecification(
                specification,
                expenseSpecificationProviderManager,
                transactionsDto);
    }
}
