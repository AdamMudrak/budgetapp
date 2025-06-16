package com.example.budgetingapp.services.utils;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_ACCOUNT_CURRENCY;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_ACCOUNT_NAME;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_EXPENSE_CATEGORIES_LIST;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_INCOME_CATEGORIES_LIST;

import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.Role;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.entities.categories.IncomeCategory;
import com.example.budgetingapp.repositories.AccountRepository;
import com.example.budgetingapp.repositories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.IncomeCategoryRepository;
import com.example.budgetingapp.repositories.RoleRepository;
import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignDefaultUserObjectsUtil {
    private final AccountRepository accountRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final RoleRepository roleRepository;

    public void assignUserRole(User user) {
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));
    }

    public void assignDefaultAccount(User user) {
        Account account = new Account();
        account.setName(DEFAULT_ACCOUNT_NAME);
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(DEFAULT_ACCOUNT_CURRENCY);
        account.setByDefault(true);
        accountRepository.save(account);
    }

    public void assignDefaultExpenseCategories(User user) {
        for (String categoryName : DEFAULT_EXPENSE_CATEGORIES_LIST) {
            ExpenseCategory expenseCategory = new ExpenseCategory();
            expenseCategory.setName(categoryName);
            expenseCategory.setUser(user);
            expenseCategoryRepository.save(expenseCategory);
        }
    }

    public void assignDefaultIncomeCategories(User user) {
        for (String categoryName : DEFAULT_INCOME_CATEGORIES_LIST) {
            IncomeCategory incomeCategory = new IncomeCategory();
            incomeCategory.setName(categoryName);
            incomeCategory.setUser(user);
            incomeCategoryRepository.save(incomeCategory);
        }
    }
}
