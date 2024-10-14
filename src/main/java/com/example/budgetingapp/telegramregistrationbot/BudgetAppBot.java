package com.example.budgetingapp.telegramregistrationbot;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;
import static com.example.budgetingapp.constants.security.SecurityConstants.BOT_NAME;
import static com.example.budgetingapp.constants.security.SecurityConstants.BOT_TO_SERVER_URI;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONTENT_TYPE;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONTENT_TYPE_HEADER;
import static com.example.budgetingapp.constants.security.SecurityConstants.FAILED;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_ACTION_JWT_STRENGTH;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_STRENGTH;
import static com.example.budgetingapp.constants.security.SecurityConstants.START;
import static com.example.budgetingapp.constants.security.SecurityConstants.STOP;
import static com.example.budgetingapp.constants.security.SecurityConstants.STOPPED_SUCCESS;
import static com.example.budgetingapp.constants.security.SecurityConstants.TELEGRAM_REGISTRATION;
import static com.example.budgetingapp.constants.security.SecurityConstants.TOKEN;
import static com.example.budgetingapp.constants.security.SecurityConstants.UNKNOWN_COMMAND;

import com.example.budgetingapp.entities.ActionToken;
import com.example.budgetingapp.exceptions.LoginException;
import com.example.budgetingapp.repositories.actiontoken.ActionTokenRepository;
import com.example.budgetingapp.security.RandomStringUtil;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BudgetAppBot extends TelegramLongPollingBot {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final RandomStringUtil randomStringUtil;
    private final ActionTokenRepository actionTokenRepository;
    private final JwtStrategy jwtStrategy;
    @Value(BOT_TO_SERVER_URI)
    private String botToServerUri;
    private boolean isBotActive;

    public BudgetAppBot(@Value(TOKEN) String token,
                        @Autowired RandomStringUtil randomStringUtil,
                        @Autowired ActionTokenRepository actionTokenRepository,
                        @Autowired JwtStrategy jwtStrategy) {
        super(token);
        getBotUsername();
        this.randomStringUtil = randomStringUtil;
        this.actionTokenRepository = actionTokenRepository;
        this.jwtStrategy = jwtStrategy;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()
                && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            switch (command) {
                case START -> {
                    isBotActive = true;
                    requestContact(update);
                }
                case STOP -> {
                    if (isBotActive) {
                        isBotActive = false;
                        handleStop(update);
                    }
                }
                default -> handleUnknownCommand(update);

            }
        } else if (update.hasMessage()
                && update.getMessage().hasContact()) {
            if (isBotActive) {
                handleContact(update.getMessage().getContact());
            }
        }
    }

    private void requestContact(Update update) {
        KeyboardButton contactButton = new KeyboardButton("Share");
        contactButton.setRequestContact(true);

        KeyboardRow contactRow = new KeyboardRow();
        contactRow.add(contactButton);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(List.of(contactRow));

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(TELEGRAM_REGISTRATION);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleContact(Contact contact) {
        String firstName = contact.getFirstName();
        String lastName = contact.getLastName();
        String phoneNumber = contact.getPhoneNumber();
        String password = randomStringUtil.generateRandomString(RANDOM_PASSWORD_STRENGTH);
        if (phoneNumber.isBlank()) {
            throw new RuntimeException("Phone can't be empty!");
        }
        String statusCode = sendRegisterOrLoginRequest(firstName, lastName, phoneNumber, password);
        String response = getResponseFromController(statusCode, firstName,
                lastName, phoneNumber, password);
        sendMarkedDownMessage(contact.getUserId(), response);
    }

    private void handleStop(Update update) {
        sendMessage(update.getMessage().getChatId(), STOPPED_SUCCESS);
    }

    private void handleUnknownCommand(Update update) {
        if (!isBotActive) {
            return;
        }
        sendMessage(update.getMessage().getChatId(),
                UNKNOWN_COMMAND);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMarkedDownMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("MarkdownV2");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String sendRegisterOrLoginRequest(String firstName,
                                            String lastName,
                                            String phoneNumber,
                                            String password) {
        String token = setActionTokenForCurrentRequest();
        String requestBody = formRequestBody(firstName, lastName, phoneNumber,
                    password, token);

        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(botToServerUri))
                    .header(CONTENT_TYPE_HEADER, CONTENT_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new LoginException("Couldn't login via telegram with root cause:"
                    + e.getCause() + e.getMessage());
        }
        return String.valueOf(response.statusCode());
    }

    private String setActionTokenForCurrentRequest() {
        JwtAbstractUtil jwtAbstractUtil = jwtStrategy.getStrategy(ACTION);
        String token = jwtAbstractUtil.generateToken(randomStringUtil
                .generateRandomString(RANDOM_ACTION_JWT_STRENGTH));
        ActionToken actionToken = new ActionToken();
        actionToken.setActionToken(token);
        actionTokenRepository.save(actionToken);
        return token;
    }

    private String formTelegramResponse(String firstName, String lastName,
                                        String phoneNumber, String password) {
        return "First name: " + firstName + System.lineSeparator()
                + "Last name: " + lastName + System.lineSeparator()
                + "Phone number: " + "`" + phoneNumber + "`" + System.lineSeparator()
                + "Password: " + "`" + password + "`" + System.lineSeparator()
                + "You can use your phone number and password to login\\." + System.lineSeparator()
                + "Just click on them to copy\\!";
    }

    private String formRequestBody(String firstName, String lastName, String phoneNumber,
                                   String password, String token) {
        return String.format("{\"firstName\": \"%s\", "
                        + "\"lastName\": \"%s\", "
                        + "\"userName\": \"%s\", "
                        + "\"password\": \"%s\", "
                        + "\"token\": \"%s\"}",
                firstName,
                lastName,
                phoneNumber,
                password,
                token);
    }

    private String getResponseFromController(String statusCode, String firstName,
                                             String lastName, String phoneNumber, String password) {
        if (statusCode.equals(CODE_200)) {
            return formTelegramResponse(firstName, lastName, phoneNumber, password);
        }
        return FAILED;
    }
}
