package com.example.budgetingapp.repositories.transactions.transactionsspecs;

import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.repositories.specifications.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TransactionSpecificationBuilder<T> {
    public Specification<T> getSpecification(Specification<T> specification,
                                      SpecificationProviderManager<T> specificationProviderManager,
                                      FilterTransactionByDaysDto transactionsDto) {
        if ((transactionsDto.accountId() == null
                && transactionsDto.fromDate() == null
                && transactionsDto.toDate() == null
                && transactionsDto.categoryIds() == null)) {
            return null;
        }
        if (transactionsDto.accountId() != null) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("accountId")
                    .getSpecification(new String[]{String.valueOf(transactionsDto.accountId())}));
        }
        if (transactionsDto.fromDate() != null && !transactionsDto.fromDate().isBlank()) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("fromDate")
                    .getSpecification(new String[]{transactionsDto.fromDate()}));
        }
        if (transactionsDto.toDate() != null && !transactionsDto.toDate().isBlank()) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("toDate")
                    .getSpecification(new String[]{transactionsDto.toDate()}));
        }
        if (transactionsDto.categoryIds() != null) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("categoryIds")
                    .getSpecification(transactionsDto.categoryIds()
                            .stream().map(String::valueOf).toArray(String[]::new)));
        }
        return specification;
    }
}
