package com.example.budgetingapp.controllers;

import static com.example.budgetingapp.constants.controllers.SupportControllerConstants.SUPPORT_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.SupportControllerConstants.SUPPORT_API_NAME;
import static com.example.budgetingapp.constants.dtos.SupportDtoConstants.SUPPORT_SEND_SUCCESS;

import com.example.budgetingapp.dtos.support.request.SupportRequestDto;
import com.example.budgetingapp.dtos.support.response.SupportResponseDto;
import com.example.budgetingapp.services.email.ContactUsByEmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = SUPPORT_API_NAME, description = SUPPORT_API_DESCRIPTION)
@RequestMapping("/support")
public class SupportController {
    private final ContactUsByEmailService contactUsByEmailService;

    @PostMapping("/send-request-to-email")
    public SupportResponseDto contactUsByEmail(@Valid @RequestBody
                                               SupportRequestDto contactUsByEmailDto) {
        contactUsByEmailService.sendMessageToHost(contactUsByEmailDto);
        return new SupportResponseDto(SUPPORT_SEND_SUCCESS);
    }
}
