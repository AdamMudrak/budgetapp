package com.example.budgetingapp.controllers.categories;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_201;
import static com.example.budgetingapp.constants.Constants.CODE_204;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.ADD_CATEGORY_SUMMARY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.CATEGORY_API_NAME;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.DELETE_CATEGORY_SUMMARY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.GET_ALL_CATEGORIES_SUMMARY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.SUCCESSFULLY_ADDED_CATEGORY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.SUCCESSFULLY_DELETE_CATEGORY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.SUCCESSFULLY_RETRIEVED_CATEGORIES;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.SUCCESSFULLY_UPDATE_CATEGORY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.UPDATE_CATEGORY_SUMMARY;
import static com.example.budgetingapp.constants.controllers.transactions.ExpenseControllerConstants.EXPENSE;

import com.example.budgetingapp.dtos.categories.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.categories.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.categories.response.CategoryDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = CATEGORY_API_NAME)
@RequestMapping("/expense-categories")
public class ExpenseCategoryController {
    private final CategoryService expenseCategoryService;

    public ExpenseCategoryController(@Qualifier(EXPENSE) CategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @Operation(summary = ADD_CATEGORY_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description =
            SUCCESSFULLY_ADDED_CATEGORY)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping("/add-category")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@AuthenticationPrincipal User user,
                                   @Valid @RequestBody CreateCategoryDto createCategoryDto) {
        return expenseCategoryService.saveCategory(user.getId(), createCategoryDto);
    }

    @Operation(summary = UPDATE_CATEGORY_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_UPDATE_CATEGORY)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PutMapping("/update-category/{categoryId}")
    public CategoryDto updateCategory(@AuthenticationPrincipal User user,
                                      @PathVariable @Positive Long categoryId,
                                      @Valid @RequestBody UpdateCategoryDto updateCategoryDto) {
        return expenseCategoryService.updateCategory(user.getId(), categoryId, updateCategoryDto);
    }

    @Operation(summary = GET_ALL_CATEGORIES_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_CATEGORIES)
    @GetMapping("/get-all-categories")
    public List<CategoryDto> getAllExpenseCategories(@AuthenticationPrincipal User user) {
        return expenseCategoryService.getAllCategoriesByUserId(user.getId());
    }

    @Operation(summary = DELETE_CATEGORY_SUMMARY)
    @ApiResponse(responseCode = CODE_204, description =
            SUCCESSFULLY_DELETE_CATEGORY)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @DeleteMapping("/delete-category/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@AuthenticationPrincipal User user,
                               @PathVariable @Positive Long categoryId) {
        expenseCategoryService.deleteByCategoryIdAndUserId(user.getId(), categoryId);
    }
}
