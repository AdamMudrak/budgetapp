package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
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
    public Specification<Expense> build(FilterTransactionsDto transactionsDto) {
        Specification<Expense> specification = Specification.where(null);
        if (transactionsDto.getAccountId() == null
                && transactionsDto.getCategoryIds() == null
                && transactionsDto.getFromDate() == null
                && transactionsDto.getToDate() == null) {
            throw new RuntimeException("Can't build specification because " //TODO заменить runtime
                    + " all of the parameters are typed null");
        }
        if (transactionsDto.getAccountId() != null) {
            specification = specification.and(expenseSpecificationProviderManager
                    .getSpecificationProvider("accountId")
                    .getSpecification(new String[]{
                            String.valueOf(transactionsDto.getAccountId())}));
        }
        if (transactionsDto.getCategoryIds() != null) {
            String[] categoryIds = new String[transactionsDto.getCategoryIds().size()];
            int counter = 0;
            for (Long id : transactionsDto.getCategoryIds()) {
                categoryIds[counter++] = String.valueOf(id);
            }
            specification = specification.and(expenseSpecificationProviderManager
                    .getSpecificationProvider("categoryIds")
                    .getSpecification(categoryIds));
        }
        if (transactionsDto.getFromDate() != null) {
            specification = specification.and(expenseSpecificationProviderManager
                    .getSpecificationProvider("fromDate")
                    .getSpecification(new String[]{
                            String.valueOf(transactionsDto.getFromDate())}));
        }
        if (transactionsDto.getToDate() != null) {
            specification = specification.and(expenseSpecificationProviderManager
                    .getSpecificationProvider("toDate")
                    .getSpecification(new String[]{
                            String.valueOf(transactionsDto.getToDate())}));
        }
        return specification;
    }
}
