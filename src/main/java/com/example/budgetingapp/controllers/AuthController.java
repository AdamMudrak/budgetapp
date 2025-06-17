package com.example.budgetingapp.controllers;

import static com.example.budgetingapp.constants.Constants.ACCESS_DENIED;
import static com.example.budgetingapp.constants.Constants.AUTHORIZATION_REQUIRED;
import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.Constants.CODE_201;
import static com.example.budgetingapp.constants.Constants.CODE_400;
import static com.example.budgetingapp.constants.Constants.CODE_403;
import static com.example.budgetingapp.constants.Constants.INVALID_ENTITY_VALUE;
import static com.example.budgetingapp.constants.Constants.ROLE_USER;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.AUTH_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.AUTH_API_NAME;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.CHANGE_PASSWORD_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.CONFIRM_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.EMAIL_LOGIN_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.INITIATE_PASSWORD_RESET_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.REFRESH_ACCESS_TOKEN_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.REGISTER_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.RESET_PASSWORD_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_CHANGE_PASSWORD;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_CONFIRMED;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_INITIATED_PASSWORD_RESET;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_LOGGED_IN;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_REFRESHED_TOKEN;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_REGISTERED;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.SUCCESSFULLY_RESET_PASSWORD;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.TELEGRAM_AUTH_SUMMARY;
import static com.example.budgetingapp.constants.controllers.AuthControllerConstants.TELEGRAM_LOGIN_SUMMARY;

import com.example.budgetingapp.constants.Constants;
import com.example.budgetingapp.dtos.users.request.SetNewPasswordDto;
import com.example.budgetingapp.dtos.users.request.TelegramAuthenticationRequestDto;
import com.example.budgetingapp.dtos.users.request.UserGetLinkToResetPasswordDto;
import com.example.budgetingapp.dtos.users.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.users.request.userlogindtos.UserEmailLoginDto;
import com.example.budgetingapp.dtos.users.request.userlogindtos.UserTelegramLoginDto;
import com.example.budgetingapp.dtos.users.response.AccessTokenDto;
import com.example.budgetingapp.dtos.users.response.StartPasswordResetDto;
import com.example.budgetingapp.dtos.users.response.TelegramAuthenticationResponseDto;
import com.example.budgetingapp.dtos.users.response.UserLoginDto;
import com.example.budgetingapp.dtos.users.response.UserRegistrationResponseDto;
import com.example.budgetingapp.exceptions.RegistrationException;
import com.example.budgetingapp.security.authentication.AuthenticationService;
import com.example.budgetingapp.security.authentication.TelegramAuthenticationService;
import com.example.budgetingapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = AUTH_API_NAME,
        description = AUTH_API_DESCRIPTION)
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final TelegramAuthenticationService telegramAuthenticationService;

    @Operation(summary = REGISTER_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description =
            SUCCESSFULLY_REGISTERED)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = CONFIRM_SUMMARY, hidden = true)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_CONFIRMED)
    @ApiResponse(responseCode = CODE_403, description = ACCESS_DENIED)
    @GetMapping("/register-success")
    public ResponseEntity<Void> confirmRegistration(HttpServletRequest httpServletRequest) {
        return userService.confirmRegistration(httpServletRequest);
    }

    @Operation(summary = TELEGRAM_LOGIN_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_LOGGED_IN)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @ApiResponse(responseCode = CODE_403, description = ACCESS_DENIED)
    @PostMapping("/login-telegram")
    public UserLoginDto loginTelegram(@RequestBody @Valid
                                              UserTelegramLoginDto request) {
        return authenticationService.authenticateTelegram(request);
    }

    @Operation(summary = EMAIL_LOGIN_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_LOGGED_IN)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @ApiResponse(responseCode = CODE_403, description = ACCESS_DENIED)
    @PostMapping("/login-email")
    public UserLoginDto loginEmail(@RequestBody @Valid UserEmailLoginDto request) {
        return authenticationService.authenticateEmail(request);
    }

    @Operation(summary = INITIATE_PASSWORD_RESET_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_INITIATED_PASSWORD_RESET)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @PostMapping("/forgot-password")
    public StartPasswordResetDto initiatePasswordReset(@Valid @RequestBody
                                                            UserGetLinkToResetPasswordDto request) {
        return authenticationService.initiatePasswordReset(request.email());
    }

    @Operation(summary = RESET_PASSWORD_SUMMARY, hidden = true)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_RESET_PASSWORD)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @GetMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(HttpServletRequest httpServletRequest) {
        return authenticationService.confirmResetPassword(httpServletRequest);
    }

    @Operation(summary = CHANGE_PASSWORD_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_CHANGE_PASSWORD)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @ApiResponse(responseCode = Constants.CODE_401, description = AUTHORIZATION_REQUIRED)
    @ApiResponse(responseCode = CODE_403, description = ACCESS_DENIED)
    @PreAuthorize(ROLE_USER)
    @PostMapping("/change-password")
    public StartPasswordResetDto changePassword(HttpServletRequest httpServletRequest,
                                                @RequestBody @Valid SetNewPasswordDto request) {
        return authenticationService.changePassword(httpServletRequest, request);
    }

    @Operation(summary = TELEGRAM_AUTH_SUMMARY, hidden = true)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM)
    @ApiResponse(responseCode = CODE_400, description = INVALID_ENTITY_VALUE)
    @ApiResponse(responseCode = CODE_403, description = ACCESS_DENIED)
    @PostMapping("/telegram-auth")
    public TelegramAuthenticationResponseDto telegramAuth(@RequestBody @Valid
                              TelegramAuthenticationRequestDto telegramAuthenticationRequestDto) {
        return telegramAuthenticationService.registerOrGetLogin(telegramAuthenticationRequestDto);
    }

    @Operation(summary = REFRESH_ACCESS_TOKEN_SUMMARY, hidden = true)
    @ApiResponse(responseCode = CODE_200, description =
            SUCCESSFULLY_REFRESHED_TOKEN)
    @ApiResponse(responseCode = CODE_403, description = ACCESS_DENIED)
    @PostMapping("/refresh-access-token")
    public AccessTokenDto refreshToken(HttpServletRequest httpServletRequest) {
        return authenticationService.refreshToken(httpServletRequest);
    }
}
