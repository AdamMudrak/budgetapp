package com.example.budgetingapp.controllers;

import com.example.budgetingapp.constants.Constants;
import com.example.budgetingapp.constants.controllers.AuthControllerConstants;
import com.example.budgetingapp.dtos.user.request.TelegramAuthenticationRequestDto;
import com.example.budgetingapp.dtos.user.request.UserGetLinkToSetRandomPasswordRequestDto;
import com.example.budgetingapp.dtos.user.request.UserLoginRequestDto;
import com.example.budgetingapp.dtos.user.request.UserRegistrationRequestDto;
import com.example.budgetingapp.dtos.user.request.UserSetNewPasswordRequestDto;
import com.example.budgetingapp.dtos.user.response.AccessTokenResponseDto;
import com.example.budgetingapp.dtos.user.response.TelegramAuthenticationResponseDto;
import com.example.budgetingapp.dtos.user.response.UserLoginResponseDto;
import com.example.budgetingapp.dtos.user.response.UserPasswordResetResponseDto;
import com.example.budgetingapp.dtos.user.response.UserRegistrationResponseDto;
import com.example.budgetingapp.exceptions.RegistrationException;
import com.example.budgetingapp.security.RandomParamFromHttpRequestUtil;
import com.example.budgetingapp.security.services.AuthenticationService;
import com.example.budgetingapp.security.services.TelegramAuthenticationService;
import com.example.budgetingapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = AuthControllerConstants.AUTH_API_NAME,
        description = AuthControllerConstants.AUTH_API_DESCRIPTION)
@RequestMapping(AuthControllerConstants.AUTH)
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final TelegramAuthenticationService telegramAuthenticationService;
    private final RandomParamFromHttpRequestUtil randomParamFromHttpRequestUtil;

    @Operation(summary = AuthControllerConstants.REGISTER_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_REGISTERED)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @PostMapping(AuthControllerConstants.REGISTER)
    public UserRegistrationResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = AuthControllerConstants.CONFIRM_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_CONFIRMED)
    @ApiResponse(responseCode = Constants.CODE_403, description = Constants.ACCESS_DENIED)
    @GetMapping(AuthControllerConstants.CONFIRM_REGISTRATION)
    public UserRegistrationResponseDto confirmRegistration(HttpServletRequest httpServletRequest) {
        randomParamFromHttpRequestUtil.parseRandomParameterAndToken(httpServletRequest);
        return userService
                .confirmRegistration(randomParamFromHttpRequestUtil.getTokenFromRepo(
                        randomParamFromHttpRequestUtil.getRandomParameter(),
                        randomParamFromHttpRequestUtil.getToken()));
    }

    @Operation(summary = AuthControllerConstants.LOGIN_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_LOGGED_IN)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @ApiResponse(responseCode = Constants.CODE_403, description = Constants.ACCESS_DENIED)
    @PostMapping(AuthControllerConstants.LOGIN)
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @Operation(summary = AuthControllerConstants.INITIATE_PASSWORD_RESET_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_INITIATED_PASSWORD_RESET)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @PostMapping(AuthControllerConstants.FORGOT_PASSWORD)
    public UserPasswordResetResponseDto initiatePasswordReset(@RequestBody
                                          @Valid UserGetLinkToSetRandomPasswordRequestDto request) {
        return authenticationService.initiatePasswordReset(request.email());
    }

    @Operation(summary = AuthControllerConstants.RESET_PASSWORD_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_RESET_PASSWORD)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @GetMapping(AuthControllerConstants.RESET_PASSWORD)
    public UserPasswordResetResponseDto resetPassword(HttpServletRequest httpServletRequest) {
        randomParamFromHttpRequestUtil.parseRandomParameterAndToken(httpServletRequest);
        return authenticationService
                .confirmResetPassword(randomParamFromHttpRequestUtil.getTokenFromRepo(
                randomParamFromHttpRequestUtil.getRandomParameter(),
                randomParamFromHttpRequestUtil.getToken()));
    }

    @Operation(summary = AuthControllerConstants.CHANGE_PASSWORD_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_CHANGE_PASSWORD)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @ApiResponse(responseCode = Constants.CODE_401, description = Constants.AUTHORIZATION_REQUIRED)
    @ApiResponse(responseCode = Constants.CODE_403, description = Constants.ACCESS_DENIED)
    @PreAuthorize(AuthControllerConstants.ROLE_USER)
    @PostMapping(AuthControllerConstants.CHANGE_PASSWORD)
    public UserPasswordResetResponseDto changePassword(HttpServletRequest httpServletRequest,
                                 @RequestBody @Valid UserSetNewPasswordRequestDto request) {
        return authenticationService.changePassword(httpServletRequest, request);
    }

    @Operation(summary = AuthControllerConstants.TELEGRAM_AUTH_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_AUTHENTICATED_VIA_TELEGRAM)
    @ApiResponse(responseCode = Constants.CODE_400, description = Constants.INVALID_ENTITY_VALUE)
    @ApiResponse(responseCode = Constants.CODE_403, description = Constants.ACCESS_DENIED)
    @PostMapping(AuthControllerConstants.TELEGRAM_AUTH)
    public TelegramAuthenticationResponseDto telegramAuth(@RequestBody @Valid
                              TelegramAuthenticationRequestDto telegramAuthenticationRequestDto) {
        return telegramAuthenticationService.registerOrLogin(telegramAuthenticationRequestDto);
    }

    @Operation(summary = AuthControllerConstants.REFRESH_ACCESS_TOKEN_SUMMARY)
    @ApiResponse(responseCode = Constants.CODE_200, description =
            AuthControllerConstants.SUCCESSFULLY_REFRESHED_TOKEN)
    @ApiResponse(responseCode = Constants.CODE_403, description = Constants.ACCESS_DENIED)
    @PostMapping(AuthControllerConstants.REFRESH_ACCESS_TOKEN)
    public AccessTokenResponseDto refreshToken(HttpServletRequest httpServletRequest) {
        return authenticationService.refreshToken(httpServletRequest);
    }
}
