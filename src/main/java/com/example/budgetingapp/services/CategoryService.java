package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.transactions.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseCategoryDto;
import java.util.List;

public interface CategoryService {
    ResponseCategoryDto saveCategory(Long userId, CreateCategoryDto createCategoryDto);

    ResponseCategoryDto updateCategory(Long userId, Long categoryId,
                                       UpdateCategoryDto updateCategoryDto);

    List<ResponseCategoryDto> getAllCategoriesByUserId(Long userId);

    void deleteByCategoryIdAndUserId(Long userId, Long categoryId);
}
