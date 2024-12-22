package com.example.budgetingapp.controllers;

import static com.example.budgetingapp.constants.Constants.SUPPORT_RESET_SENT_LINK;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ACCOUNT;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ACCOUNT_API_NAME;
import static com.example.budgetingapp.constants.controllers.SupportControllerConstants.SEND_REQUEST_TO_EMAIL;
import static com.example.budgetingapp.constants.controllers.SupportControllerConstants.SUPPORT;
import static com.example.budgetingapp.constants.controllers.SupportControllerConstants.SUPPORT_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.SupportControllerConstants.SUPPORT_API_NAME;

import com.example.budgetingapp.dtos.emails.ContactUsByEmailDto;
import com.example.budgetingapp.services.RedirectUtil;
import com.example.budgetingapp.services.email.ContactUsEmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = SUPPORT_API_NAME, description = SUPPORT_API_DESCRIPTION)
@RequestMapping(SUPPORT)
public class SupportController {
    private final ContactUsEmailService contactUsEmailService;
    private final RedirectUtil redirectUtil;

    @GetMapping(SEND_REQUEST_TO_EMAIL)
    public ResponseEntity<Void> contactUsByEmail(ContactUsByEmailDto contactUsByEmailDto) {
        contactUsEmailService.sendMessageToHost(contactUsByEmailDto);
        return redirectUtil.redirect(SUPPORT_RESET_SENT_LINK);
    }
}
