package com.example.budgetingapp.services.impl.transactions.incomes;

import com.example.budgetingapp.dtos.transactions.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseCategoryDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.entities.transactions.incomes.IncomeCategory;
import com.example.budgetingapp.exceptions.AlreadyExistsException;
import com.example.budgetingapp.exceptions.ConflictException;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.CategoryMapper;
import com.example.budgetingapp.repositories.transactions.incomes.IncomeCategoryRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomeCategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseCategoryDto saveCategory(Long userId, CreateCategoryDto createCategoryDto) {
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
        incomeCategory.setName(createCategoryDto.newName());
        return categoryMapper
                .toIncomeCategoryDto(incomeCategoryRepository
                        .save(incomeCategory));
    }

    @Override
    public List<ResponseCategoryDto> getAllCategoriesByUserId(Long userId) {
        return categoryMapper
                .toIncomeCategoryDtoList(incomeCategoryRepository
                        .getAllByUserId(userId));
    }

    @Override
    public void deleteByCategoryIdAndUserId(Long userId, Long categoryId) {
        incomeCategoryRepository.deleteByIdAndUserId(categoryId, userId);
    }
}
