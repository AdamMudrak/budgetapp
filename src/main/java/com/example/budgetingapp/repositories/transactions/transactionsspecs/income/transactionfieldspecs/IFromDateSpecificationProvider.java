package com.example.budgetingapp.repositories.transactions.transactionsspecs.income.transactionfieldspecs;

import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IFromDateSpecificationProvider implements SpecificationProvider<Income> {

    @Override
    public String getKey() {
        return "fromDate";
    }

    @Override
    public Specification<Income> getSpecification(String[] params) {
        LocalDate fromDate = LocalDate.parse(params[0]);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), fromDate));
    }
}
