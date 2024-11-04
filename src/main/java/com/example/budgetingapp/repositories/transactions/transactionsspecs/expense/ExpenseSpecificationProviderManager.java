package com.example.budgetingapp.repositories.transactions.transactionsspecs.expense;

import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.exceptions.notfoundexceptions.SpecificationProviderNotFoundException;
import com.example.budgetingapp.repositories.specifications.SpecificationProvider;
import com.example.budgetingapp.repositories.specifications.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExpenseSpecificationProviderManager implements SpecificationProviderManager<Expense> {
    private final List<SpecificationProvider<Expense>> expenseSpecificationProviders;

    @Override
    public SpecificationProvider<Expense> getSpecificationProvider(String key) {
        return expenseSpecificationProviders.stream()
                .filter(expenseSpecificationProvider ->
                        expenseSpecificationProvider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationProviderNotFoundException(
                        "Can't find correct specification provider for key : " + key));
    }
}
