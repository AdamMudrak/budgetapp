package com.example.budgetingapp.services.implementations.categories;

import static com.example.budgetingapp.constants.controllers.transactions.IncomeControllerConstants.INCOME;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.CATEGORY_QUANTITY_THRESHOLD;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.DEFAULT_CATEGORY_NAME;

import com.example.budgetingapp.dtos.categories.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.categories.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.categories.response.ResponseCategoryDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.categories.IncomeCategory;
import com.example.budgetingapp.exceptions.conflictexpections.AlreadyExistsException;
import com.example.budgetingapp.exceptions.conflictexpections.ConflictException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.CategoryMapper;
import com.example.budgetingapp.repositories.categories.IncomeCategoryRepository;
import com.example.budgetingapp.repositories.transactions.IncomeRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.interfaces.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Qualifier(INCOME)
@RequiredArgsConstructor
public class IncomeCategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;

    @Override
    public ResponseCategoryDto saveCategory(Long userId, CreateCategoryDto createCategoryDto) {
        if (incomeCategoryRepository.countCategoriesByUserId(userId)
                >= CATEGORY_QUANTITY_THRESHOLD) {
            throw new ConflictException("You can't have more than " + CATEGORY_QUANTITY_THRESHOLD
                    + " income categories!");
        }
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        if (incomeCategoryRepository.existsByNameAndUserId(createCategoryDto.name(), userId)) {
            throw new ConflictException("Income category with name "
                    + createCategoryDto.name() + " already exists");
        }
        IncomeCategory incomeCategory =
                categoryMapper.toIncomeCategory(createCategoryDto);
        incomeCategory.setUser(currentUser);
        incomeCategoryRepository.save(incomeCategory);
        return categoryMapper.toIncomeCategoryDto(incomeCategory);
    }

    @Override
    public ResponseCategoryDto updateCategory(Long userId, Long categoryId,
                                              UpdateCategoryDto createCategoryDto) {
        if (incomeCategoryRepository.existsByNameAndUserId(createCategoryDto.newName(), userId)) {
            throw new AlreadyExistsException("You already have income category named "
                    + createCategoryDto.newName());
        }
        IncomeCategory incomeCategory = incomeCategoryRepository.findByIdAndUserId(categoryId,
                userId).orElseThrow(
                    () -> new EntityNotFoundException("No income category with id "
                        + categoryId + " was found for user with id " + userId));
        if (incomeCategory.getName().equals(DEFAULT_CATEGORY_NAME)) {
            throw new IllegalArgumentException("Can't update category by default");
        }
        incomeCategory.setName(createCategoryDto.newName());
        return categoryMapper
                .toIncomeCategoryDto(incomeCategoryRepository
                        .save(incomeCategory));
    }

    @Override
    public List<ResponseCategoryDto> getAllCategoriesByUserId(Long userId, Pageable pageable) {
        return categoryMapper
                .toIncomeCategoryDtoList(incomeCategoryRepository
                        .findAllByUserId(userId, pageable));
    }

    @Override
    public void deleteByCategoryIdAndUserId(Long userId, Long categoryId) {
        IncomeCategory incomeCategory = incomeCategoryRepository
                .findByIdAndUserId(categoryId, userId).orElseThrow(
                        () -> new EntityNotFoundException(
                            "No category with id " + categoryId + " for user with id " + userId));
        if (incomeCategory.getName().equals(DEFAULT_CATEGORY_NAME)) {
            throw new IllegalArgumentException("Can't delete category by default");
        }
        IncomeCategory defaultCategory =
                incomeCategoryRepository.findByNameAndUserId(DEFAULT_CATEGORY_NAME, userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No default category was found for user with id " + userId));

        incomeRepository.saveAll(incomeRepository.findAllByUserId(userId)
                .stream()
                .filter(income -> income.getIncomeCategory().getId()
                        .equals(incomeCategory.getId()))
                .peek(income -> income.setIncomeCategory(defaultCategory)).toList());

        incomeCategoryRepository.deleteByIdAndUserId(categoryId, userId);
    }
}