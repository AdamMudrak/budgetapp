package com.example.budgetingapp.services.impl.categories;

import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.EXPENSE;

import com.example.budgetingapp.dtos.transactions.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseCategoryDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.categories.ExpenseCategory;
import com.example.budgetingapp.exceptions.AlreadyExistsException;
import com.example.budgetingapp.exceptions.ConflictException;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.CategoryMapper;
import com.example.budgetingapp.repositories.categories.ExpenseCategoryRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.CategoryService;
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

    @Override
    public ResponseCategoryDto saveCategory(Long userId, CreateCategoryDto createCategoryDto) {
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
    public ResponseCategoryDto updateCategory(Long userId, Long categoryId,
                                              UpdateCategoryDto createCategoryDto) {
        if (expenseCategoryRepository.existsByNameAndUserId(createCategoryDto.newName(), userId)) {
            throw new AlreadyExistsException("You already have expense category named "
                    + createCategoryDto.newName());
        }
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByIdAndUserId(categoryId,
                userId).orElseThrow(
                    () -> new EntityNotFoundException("No expense category with id "
                        + categoryId + " was found for user with id " + userId));
        expenseCategory.setName(createCategoryDto.newName());
        return categoryMapper
                .toExpenseCategoryDto(expenseCategoryRepository
                        .save(expenseCategory));
    }

    @Override
    public List<ResponseCategoryDto> getAllCategoriesByUserId(Long userId) {
        return categoryMapper
                .toExpenseCategoryDtoList(expenseCategoryRepository
                        .getAllByUserId(userId));
    }

    @Override
    public void deleteByCategoryIdAndUserId(Long userId, Long categoryId) {
        expenseCategoryRepository.deleteByIdAndUserId(categoryId, userId);
    }
}
