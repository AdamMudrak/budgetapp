package com.example.budgetingapp.controllers;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ACCOUNT;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ACCOUNT_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ACCOUNT_API_NAME;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ADD_ACCOUNT;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ADD_ACCOUNT_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.GET_ALL_ACCOUNTS;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.GET_ALL_ACCOUNTS_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.SET_ACCOUNT_BY_DEFAULT;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.SET_ACCOUNT_BY_DEFAULT_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.SUCCESSFULLY_ADDED;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.SUCCESSFULLY_RETRIEVED;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.SUCCESSFULLY_SET;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.SUCCESSFULLY_UPDATED;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.UPDATE_ACCOUNT;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.UPDATE_ACCOUNT_SUMMARY;

import com.example.budgetingapp.dtos.account.request.CreateAccountDto;
import com.example.budgetingapp.dtos.account.request.UpdateAccountDto;
import com.example.budgetingapp.dtos.account.response.AccountDto;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize(ROLE_USER)
@RequiredArgsConstructor
@RestController
@Tag(name = ACCOUNT_API_NAME,
        description = ACCOUNT_API_DESCRIPTION)
@RequestMapping(ACCOUNT)
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = ADD_ACCOUNT_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_ADDED)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(ADD_ACCOUNT)
    public AccountDto addAccount(@AuthenticationPrincipal User user,
            @RequestBody CreateAccountDto createAccountDto) {
        return accountService.save(user.getId(), createAccountDto);
    }

    @Operation(summary = GET_ALL_ACCOUNTS_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_RETRIEVED)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(GET_ALL_ACCOUNTS)
    public List<AccountDto> getAllAccounts(@AuthenticationPrincipal User user) {
        return accountService.getAllByUserId(user.getId());
    }

    @Operation(summary = UPDATE_ACCOUNT_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_UPDATED)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(UPDATE_ACCOUNT)
    public AccountDto updateAccount(@AuthenticationPrincipal User user,
                                     @RequestBody UpdateAccountDto updateAccountDto) {
        return accountService.update(user.getId(), updateAccountDto);
    }

    @Operation(summary = SET_ACCOUNT_BY_DEFAULT_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = SUCCESSFULLY_SET)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping(SET_ACCOUNT_BY_DEFAULT)
    public AccountDto setAccountByDefault(@AuthenticationPrincipal User user,
                                    @RequestBody UpdateAccountDto updateAccountDto) {
        return accountService.update(user.getId(), updateAccountDto);
    }
}
