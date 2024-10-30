package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.transactionfieldspecs;

import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.utils.FromDateParseUtil;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EFromDateSpecificationProvider implements SpecificationProvider<Expense> {
    private final FromDateParseUtil fromDateParseUtil;

    @Override
    public String getKey() {
        return "fromDate";
    }

    @Override
    public Specification<Expense> getSpecification(String[] params) {
        LocalDate fromDate = fromDateParseUtil.parseDate(params);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), fromDate));
    }
}
