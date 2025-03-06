package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense;

import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.repositories.specifications.SpecificationBuilder;
import com.example.budgetingapp.repositories.specifications.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExpenseSpecificationBuilder implements SpecificationBuilder<Expense> {
    private final SpecificationProviderManager<Expense> expenseSpecificationProviderManager;

    @Override
    public Specification<Expense> build(FilterTransactionByDaysDto transactionsDto) {
        Specification<Expense> specification = Specification.where(null);
        if ((transactionsDto.accountId() == null
                && transactionsDto.fromDate() == null
                && transactionsDto.toDate() == null
                && transactionsDto.categoryIds() == null)) {
            return null;
        }
        if (transactionsDto.accountId() != null) {
            specification = specification.and(expenseSpecificationProviderManager
                    .getSpecificationProvider("accountId")
                    .getSpecification(new String[]{String.valueOf(transactionsDto.accountId())}));
        }
        if (transactionsDto.fromDate() != null && !transactionsDto.fromDate().isBlank()) {
            specification = specification.and(expenseSpecificationProviderManager
                    .getSpecificationProvider("fromDate")
                    .getSpecification(new String[]{transactionsDto.fromDate()}));
        }
        if (transactionsDto.toDate() != null && !transactionsDto.toDate().isBlank()) {
            specification = specification.and(expenseSpecificationProviderManager
                    .getSpecificationProvider("toDate")
                    .getSpecification(new String[]{transactionsDto.toDate()}));
        }
        if (transactionsDto.categoryIds() != null) {
            specification = specification.and(expenseSpecificationProviderManager
                    .getSpecificationProvider("categoryIds")
                    .getSpecification(transactionsDto.categoryIds()
                            .stream().map(String::valueOf).toArray(String[]::new)));
        }
        return specification;
    }
}
