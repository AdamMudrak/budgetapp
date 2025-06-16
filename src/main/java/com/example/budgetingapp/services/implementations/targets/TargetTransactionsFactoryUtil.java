package com.example.budgetingapp.services.implementations.targets;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.TARGET_EXPENSE_CATEGORY;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.TARGET_INCOME_CATEGORY;

import com.example.budgetingapp.dtos.targets.request.ReplenishTargetDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.Target;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.Expense;
import com.example.budgetingapp.entities.transactions.Income;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.repositories.categories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.categories.IncomeCategoryRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class TargetTransactionsFactoryUtil {
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;

    protected Expense formExpense(ReplenishTargetDto requestDto,
                                  Account account,
                                  User user,
                                  String targetName) {
        Expense replenishmentExpense = new Expense();
        replenishmentExpense.setAccount(account);
        replenishmentExpense.setCurrency(account.getCurrency());
        replenishmentExpense.setAmount(requestDto.sumOfReplenishment());
        replenishmentExpense.setTransactionDate(LocalDate.now());
        replenishmentExpense.setComment(targetName);

        replenishmentExpense.setExpenseCategory(expenseCategoryRepository
                .findByNameAndUserId(TARGET_EXPENSE_CATEGORY, user.getId()).orElseThrow(
                        () -> new EntityNotFoundException("No category with name "
                                + TARGET_EXPENSE_CATEGORY + " for user " + user.getId()
                                + " was found")));

        replenishmentExpense.setUser(user);
        return replenishmentExpense;
    }

    protected Income formIncome(Target target,
                                Account account,
                                User user) {
        Income deletedTargetIncome = new Income();
        deletedTargetIncome.setAccount(account);
        deletedTargetIncome.setCurrency(account.getCurrency());
        deletedTargetIncome.setAmount(target.getCurrentSum());
        deletedTargetIncome.setTransactionDate(LocalDate.now());
        deletedTargetIncome.setComment(target.getName());
        deletedTargetIncome.setIncomeCategory(incomeCategoryRepository
                .findByNameAndUserId(TARGET_INCOME_CATEGORY, user.getId()).orElseThrow(
                        () -> new EntityNotFoundException("No category with name "
                                + TARGET_INCOME_CATEGORY + " for user " + user.getId()
                                + " was found")));
        deletedTargetIncome.setUser(user);
        return deletedTargetIncome;
    }
}
