package com.example.budgetingapp.repositories.transactions.transactionsspecs.income;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.repositories.specifications.SpecificationBuilder;
import com.example.budgetingapp.repositories.specifications.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IncomeSpecificationBuilder implements SpecificationBuilder<Income> {
    private final SpecificationProviderManager<Income> incomeSpecificationProviderManager;

    @Override
    public Specification<Income> build(FilterTransactionsDto transactionsDto) {
        Specification<Income> specification = Specification.where(null);
        if (transactionsDto.getAccountId().isBlank()
                && transactionsDto.getFromDate().isBlank()
                && transactionsDto.getToDate().isBlank()
                && transactionsDto.getCategoryIds().length == 0) {
            return null;
        }
        if (!transactionsDto.getAccountId().isBlank()) {
            specification = specification.and(incomeSpecificationProviderManager
                    .getSpecificationProvider("accountId")
                    .getSpecification(new String[]{transactionsDto.getAccountId()}));
        }
        if (!transactionsDto.getFromDate().isBlank()) {
            specification = specification.and(incomeSpecificationProviderManager
                    .getSpecificationProvider("fromDate")
                    .getSpecification(new String[]{transactionsDto.getFromDate()}));
        }
        if (!transactionsDto.getToDate().isBlank()) {
            specification = specification.and(incomeSpecificationProviderManager
                    .getSpecificationProvider("toDate")
                    .getSpecification(new String[]{transactionsDto.getToDate()}));
        }
        if (transactionsDto.getCategoryIds().length != 0) {
            specification = specification.and(incomeSpecificationProviderManager
                    .getSpecificationProvider("categoryIds")
                    .getSpecification(transactionsDto.getCategoryIds()));
        }
        return specification;
    }
}
