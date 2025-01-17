package com.example.budgetingapp.services.interfaces;

import com.example.budgetingapp.dtos.categories.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.categories.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.categories.response.CategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto saveCategory(Long userId, CreateCategoryDto createCategoryDto);

    CategoryDto updateCategory(Long userId, Long categoryId,
                               UpdateCategoryDto updateCategoryDto);

    List<CategoryDto> getAllCategoriesByUserId(Long userId, Pageable pageable);

    void deleteByCategoryIdAndUserId(Long userId, Long categoryId);
}
