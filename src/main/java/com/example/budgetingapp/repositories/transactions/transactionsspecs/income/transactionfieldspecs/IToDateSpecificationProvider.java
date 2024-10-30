package com.example.budgetingapp.repositories.transactions.transactionsspecs.income.transactionfieldspecs;

import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import com.example.budgetingapp.repositories.transactions.transactionsspecs.utils.ToDateParseUtil;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IToDateSpecificationProvider implements SpecificationProvider<Income> {
    private final ToDateParseUtil toDateParseUtil;

    @Override
    public String getKey() {
        return "toDate";
    }

    @Override
    public Specification<Income> getSpecification(String[] params) {
        LocalDate toDate = toDateParseUtil.parseDate(params);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), toDate));
    }
}
