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
        if ((transactionsDto.accountId() == null
                && transactionsDto.fromDate() == null
                && transactionsDto.toDate() == null
                && transactionsDto.categoryIds() == null)
                || (transactionsDto.accountId() != null
                && transactionsDto.accountId().isBlank()
                && transactionsDto.fromDate() != null
                && transactionsDto.fromDate().isBlank()
                && transactionsDto.toDate() != null
                && transactionsDto.toDate().isBlank()
                && transactionsDto.categoryIds() != null
                && transactionsDto.categoryIds().length == 0)) {
            return null;
        }
        if (transactionsDto.accountId() != null && !transactionsDto.accountId().isBlank()) {
            specification = specification.and(incomeSpecificationProviderManager
                    .getSpecificationProvider("accountId")
                    .getSpecification(new String[]{transactionsDto.accountId()}));
        }
        if (transactionsDto.fromDate() != null && !transactionsDto.fromDate().isBlank()) {
            specification = specification.and(incomeSpecificationProviderManager
                    .getSpecificationProvider("fromDate")
                    .getSpecification(new String[]{transactionsDto.fromDate()}));
        }
        if (transactionsDto.toDate() != null && !transactionsDto.toDate().isBlank()) {
            specification = specification.and(incomeSpecificationProviderManager
                    .getSpecificationProvider("toDate")
                    .getSpecification(new String[]{transactionsDto.toDate()}));
        }
        if (transactionsDto.categoryIds() != null && transactionsDto.categoryIds().length != 0) {
            specification = specification.and(incomeSpecificationProviderManager
                    .getSpecificationProvider("categoryIds")
                    .getSpecification(transactionsDto.categoryIds()));
        }
        return specification;
    }
}
