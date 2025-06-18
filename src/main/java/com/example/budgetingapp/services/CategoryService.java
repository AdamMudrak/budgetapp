package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.categories.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.categories.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.categories.response.CategoryDto;
import java.util.List;

public interface CategoryService {
    CategoryDto saveCategory(Long userId, CreateCategoryDto createCategoryDto);

    CategoryDto updateCategory(Long userId, Long categoryId,
                               UpdateCategoryDto updateCategoryDto);

    List<CategoryDto> getAllCategoriesByUserId(Long userId);

    void deleteByCategoryIdAndUserId(Long userId, Long categoryId);
}
