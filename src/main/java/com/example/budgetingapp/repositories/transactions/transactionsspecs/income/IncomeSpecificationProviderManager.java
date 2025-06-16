package com.example.budgetingapp.repositories.transactions.transactionsspecs.income;

import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.exceptions.SpecificationProviderNotFoundException;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import com.example.budgetingapp.repositories.specifications.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IncomeSpecificationProviderManager implements SpecificationProviderManager<Income> {
    private final List<SpecificationProvider<Income>> incomeSpecificationProviders;

    @Override
    public SpecificationProvider<Income> getSpecificationProvider(String key) {
        return incomeSpecificationProviders.stream()
                .filter(incomeSpecificationProvider ->
                        incomeSpecificationProvider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationProviderNotFoundException(
                        "Can't find correct specification provider for key : " + key));
    }
}
