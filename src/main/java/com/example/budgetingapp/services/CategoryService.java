package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.categories.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.categories.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.categories.response.ResponseCategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    ResponseCategoryDto saveCategory(Long userId, CreateCategoryDto createCategoryDto);

    ResponseCategoryDto updateCategory(Long userId, Long categoryId,
                                       UpdateCategoryDto updateCategoryDto);

    List<ResponseCategoryDto> getAllCategoriesByUserId(Long userId, Pageable pageable);

    void deleteByCategoryIdAndUserId(Long userId, Long categoryId);
}
