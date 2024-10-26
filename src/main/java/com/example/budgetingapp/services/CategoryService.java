package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.transactions.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseCategoryDto;
import java.util.List;

public interface CategoryService {
    ResponseCategoryDto saveCategory(Long userId, CreateCategoryDto createCategoryDto);

    ResponseCategoryDto updateCategory(Long userId, UpdateCategoryDto createCategoryDto);

    List<ResponseCategoryDto> getAllCategoriesByUserId(Long userId);

    void deleteByCategoryNameAndUserId(Long userId, String name);
}
