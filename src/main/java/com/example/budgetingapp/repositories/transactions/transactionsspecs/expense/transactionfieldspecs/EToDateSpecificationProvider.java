package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense.transactionfieldspecs;

import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.utils.ToDateParseUtil;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EToDateSpecificationProvider implements SpecificationProvider<Expense> {
    private final ToDateParseUtil toDateParseUtil;

    @Override
    public String getKey() {
        return "toDate";
    }

    @Override
    public Specification<Expense> getSpecification(String[] params) {
        LocalDate toDate = toDateParseUtil.parseDate(params);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), toDate));
    }
}
