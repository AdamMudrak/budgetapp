package com.example.budgetingapp.controllers;

import static com.example.budgetingapp.security.SecurityConstants.BEGIN_INDEX;

import com.example.budgetingapp.constants.Constants;
import com.example.budgetingapp.constants.controllers.AuthControllerConstants;
import com.example.budgetingapp.dtos.user.request.UserGetLinkToSetRandomPasswordRequestDto;
import com.example.budgetingapp.dtos.user.request.UserLoginRequestDto;
import com.example.budgetingapp.dtos.user.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.user.request.UserSetNewPasswordRequestDto;
import com.example.budgetingapp.dtos.user.response.UserLoginResponseDto;
import com.example.budgetingapp.dtos.user.response.UserRegistrationResponseDto;
import com.example.budgetingapp.exceptions.RegistrationException;
import com.example.budgetingapp.security.AuthenticationService;
import com.example.budgetingapp.security.EmailSecretProvider;
import com.example.budgetingapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = AuthControllerConstants.AUTH_API_NAME,
        description = AuthControllerConstants.AUTH_API_DESCRIPTION)
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final EmailSecretProvider emailSecretProvider;

    @Operation(summary = AuthControllerConstants.REGISTER_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_REGISTERED)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @PostMapping("/register")
    public UserRegistrationResponseDto registerUser(
            @RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = AuthControllerConstants.LOGIN_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_LOGGED_IN)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @ApiResponse(responseCode = Constants.CODE_403, description = Constants.ACCESS_DENIED)
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @Operation(summary = AuthControllerConstants.INITIATE_PASSWORD_RESET_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_INITIATED_PASSWORD_RESET)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @PostMapping("/forgot-password")
    public String initiatePasswordReset(@RequestBody
                                          @Valid UserGetLinkToSetRandomPasswordRequestDto request) {
        return authenticationService.initiatePasswordReset(request.email());
    }

    @Operation(summary = AuthControllerConstants.RESET_PASSWORD_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_RESET_PASSWORD)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest httpServletRequest) {
        return authenticationService.resetPassword(httpServletRequest
                .getParameter(emailSecretProvider.getEmailSecret()));
    }

    @Operation(summary = AuthControllerConstants.LOGIN_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.CHANGE_PASSWORD_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_400, description =
            AuthControllerConstants.SUCCESSFULLY_CHANGE_PASSWORD)
    @ApiResponse(responseCode = Constants.CODE_401, description = Constants.AUTHORIZATION_REQUIRED)
    @ApiResponse(responseCode = Constants.CODE_403, description = Constants.ACCESS_DENIED)
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/change-password")
    public String changePassword(HttpServletRequest httpServletRequest,
                                 @RequestBody @Valid UserSetNewPasswordRequestDto request) {
        String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken
                .startsWith(AuthControllerConstants.BEARER)) {
            bearerToken = bearerToken.substring(BEGIN_INDEX);
        }
        return authenticationService.changePassword(bearerToken, request);
    }
}
