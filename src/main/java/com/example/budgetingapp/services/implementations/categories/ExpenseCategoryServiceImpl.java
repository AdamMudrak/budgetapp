package com.example.budgetingapp.services.implementations.categories;

import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.EXPENSE;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.CATEGORY_QUANTITY_THRESHOLD;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_CATEGORY_NAME;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.TARGET_EXPENSE_CATEGORY;

import com.example.budgetingapp.dtos.categories.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.categories.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.categories.response.CategoryDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.exceptions.AlreadyExistsException;
import com.example.budgetingapp.exceptions.ConflictException;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.CategoryMapper;
import com.example.budgetingapp.repositories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.repositories.UserRepository;
import com.example.budgetingapp.services.interfaces.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier(EXPENSE)
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public CategoryDto saveCategory(Long userId, CreateCategoryDto createCategoryDto) {
        if (expenseCategoryRepository.countCategoriesByUserId(userId)
                >= CATEGORY_QUANTITY_THRESHOLD) {
            throw new ConflictException("You can't have more than "
                    + (CATEGORY_QUANTITY_THRESHOLD - 1) + " expense categories!");
        }
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        if (expenseCategoryRepository.existsByNameAndUserId(createCategoryDto.name(), userId)) {
            throw new ConflictException("Expense category with name "
                    + createCategoryDto.name() + " already exists");
        }
        ExpenseCategory expenseCategory =
                categoryMapper.toExpenseCategory(createCategoryDto);
        expenseCategory.setUser(currentUser);
        expenseCategoryRepository.save(expenseCategory);
        return categoryMapper.toExpenseCategoryDto(expenseCategory);
    }

    @Override
    public CategoryDto updateCategory(Long userId, Long categoryId,
                                      UpdateCategoryDto createCategoryDto) {
        if (expenseCategoryRepository.existsByNameAndUserId(createCategoryDto.newName(), userId)) {
            throw new AlreadyExistsException("You already have expense category named "
                    + createCategoryDto.newName());
        }
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByIdAndUserId(categoryId,
                userId).orElseThrow(
                    () -> new EntityNotFoundException("No expense category with id "
                        + categoryId + " was found for user with id " + userId));
        if (expenseCategory.getName().equals(DEFAULT_CATEGORY_NAME)
                || expenseCategory.getName().equals(TARGET_EXPENSE_CATEGORY)) {
            throw new IllegalArgumentException("Can't update category by default");
        }
        expenseCategory.setName(createCategoryDto.newName());
        return categoryMapper
                .toExpenseCategoryDto(expenseCategoryRepository
                        .save(expenseCategory));
    }

    @Override
    public List<CategoryDto> getAllCategoriesByUserId(Long userId) {
        return categoryMapper
                .toExpenseCategoryDtoList(expenseCategoryRepository
                .findAllByUserId(userId));
    }

    @Override
    public void deleteByCategoryIdAndUserId(Long userId, Long categoryId) {
        ExpenseCategory expenseCategory = expenseCategoryRepository
                .findByIdAndUserId(categoryId, userId).orElseThrow(
                        () -> new EntityNotFoundException(
                        "No category with id " + categoryId + " for user with id " + userId));
        if (expenseCategory.getName().equals(DEFAULT_CATEGORY_NAME)
                || expenseCategory.getName().equals(TARGET_EXPENSE_CATEGORY)) {
            throw new IllegalArgumentException("Can't delete category by default");
        }
        ExpenseCategory defaultCategory =
                expenseCategoryRepository.findByNameAndUserId(DEFAULT_CATEGORY_NAME, userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No default category was found for user with id " + userId));

        expenseRepository.saveAll(expenseRepository.findAllByUserId(userId)
                .stream()
                .filter(expense -> expense.getExpenseCategory().getId()
                        .equals(expenseCategory.getId()))
                .peek(expense -> expense.setExpenseCategory(defaultCategory)).toList());

        expenseCategoryRepository.deleteByIdAndUserId(categoryId, userId);
    }
}
