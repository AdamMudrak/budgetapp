package com.example.budgetingapp.controllers.categories;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.ADD_CATEGORY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.ADD_CATEGORY_SUMMARY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.CATEGORY_API_NAME;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.DELETE_CATEGORY_BY_ID;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.DELETE_CATEGORY_SUMMARY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.GET_ALL_CATEGORIES;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.GET_ALL_CATEGORIES_SUMMARY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.INCOME_CATEGORIES;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.SUCCESSFULLY_ADDED_CATEGORY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.SUCCESSFULLY_DELETE_CATEGORY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.SUCCESSFULLY_RETRIEVED_CATEGORIES;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.SUCCESSFULLY_UPDATE_CATEGORY;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.UPDATE_CATEGORY_BY_ID;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.UPDATE_CATEGORY_SUMMARY;
import static com.example.budgetingapp.constants.controllers.TransactionControllerConstants.INCOME;

import com.example.budgetingapp.dtos.transactions.request.CreateCategoryDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateCategoryDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseCategoryDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize(ROLE_USER)
@RestController
@Tag(name = CATEGORY_API_NAME)
@RequestMapping(INCOME_CATEGORIES)
public class IncomeCategoryController {
    private final CategoryService incomeCategoryService;

    public IncomeCategoryController(@Qualifier(INCOME) CategoryService incomeCategoryService) {
        this.incomeCategoryService = incomeCategoryService;
    }

    @Operation(summary = ADD_CATEGORY_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_ADDED_CATEGORY)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(ADD_CATEGORY)
    public ResponseCategoryDto addCategory(@AuthenticationPrincipal User user,
                               @RequestBody CreateCategoryDto createCategoryDto) {
        return incomeCategoryService.saveCategory(user.getId(), createCategoryDto);
    }

    @Operation(summary = UPDATE_CATEGORY_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_UPDATE_CATEGORY)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PutMapping(UPDATE_CATEGORY_BY_ID)
    public ResponseCategoryDto updateCategory(@AuthenticationPrincipal User user,
                                           @PathVariable Long categoryId,
                                           @RequestBody UpdateCategoryDto updateCategoryDto) {
        return incomeCategoryService.updateCategory(user.getId(), categoryId, updateCategoryDto);
    }

    @Operation(summary = GET_ALL_CATEGORIES_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RETRIEVED_CATEGORIES)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping(GET_ALL_CATEGORIES)
    public List<ResponseCategoryDto> getAllIncomeCategories(@AuthenticationPrincipal User user) {
        return incomeCategoryService.getAllCategoriesByUserId(user.getId());
    }

    @Operation(summary = DELETE_CATEGORY_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_DELETE_CATEGORY)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @DeleteMapping(DELETE_CATEGORY_BY_ID)
    public void deleteCategory(@AuthenticationPrincipal User user,
                                       @PathVariable Long categoryId) {
        incomeCategoryService.deleteByCategoryIdAndUserId(user.getId(), categoryId);
    }
}
