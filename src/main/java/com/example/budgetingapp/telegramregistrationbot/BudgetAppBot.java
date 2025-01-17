package com.example.budgetingapp.telegramregistrationbot;

import static com.example.budgetingapp.constants.Constants.CODE_200;
import static com.example.budgetingapp.constants.security.SecurityConstants.ACTION;
import static com.example.budgetingapp.constants.security.SecurityConstants.BOT_NAME;
import static com.example.budgetingapp.constants.security.SecurityConstants.BOT_TO_SERVER_REQUEST_URI;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONTENT_TYPE;
import static com.example.budgetingapp.constants.security.SecurityConstants.CONTENT_TYPE_HEADER;
import static com.example.budgetingapp.constants.security.SecurityConstants.FAILED;
import static com.example.budgetingapp.constants.security.SecurityConstants.PLUS;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_ACTION_JWT_STRENGTH;
import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_STRENGTH;
import static com.example.budgetingapp.constants.security.SecurityConstants.START;
import static com.example.budgetingapp.constants.security.SecurityConstants.STOP;
import static com.example.budgetingapp.constants.security.SecurityConstants.STOPPED_SUCCESS;
import static com.example.budgetingapp.constants.security.SecurityConstants.TELEGRAM_REGISTRATION;
import static com.example.budgetingapp.constants.security.SecurityConstants.TOKEN;
import static com.example.budgetingapp.constants.security.SecurityConstants.UNKNOWN_COMMAND;

import com.example.budgetingapp.entities.tokens.ActionToken;
import com.example.budgetingapp.exceptions.forbidden.LoginException;
import com.example.budgetingapp.repositories.actiontoken.ActionTokenRepository;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import com.example.budgetingapp.security.utils.RandomStringUtil;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private static final Long TIME_OUT = 20L;
    private static final Set<Long> stoppedUserIds = new HashSet<>();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final RandomStringUtil randomStringUtil;
    private final ActionTokenRepository actionTokenRepository;
    private final JwtStrategy jwtStrategy;
    @Value(BOT_TO_SERVER_REQUEST_URI)
    private String botToServerRequestUri;

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
        if (update.hasMessage()) {
            Long currentUserId = update.getMessage().getChatId();
            if (update.getMessage().hasText()) {
                String currentUserMessage = update.getMessage().getText();
                if (currentUserMessage.equals(START)) {
                    stoppedUserIds.remove(currentUserId);
                    requestContact(update);
                } else if (!stoppedUserIds.contains(currentUserId)) {
                    if (currentUserMessage.equals(STOP)) {
                        stoppedUserIds.add(currentUserId);
                        handleStop(update);
                    } else {
                        handleUnknownCommand(update);
                    }
                }
            } else if (update.getMessage().hasContact()) {
                if (!stoppedUserIds.contains(currentUserId)) {
                    handleContact(update.getMessage().getContact());
                }
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
        String phoneNumber = contact.getPhoneNumber();
        if (!phoneNumber.startsWith(PLUS)) {
            phoneNumber = PLUS + phoneNumber;
        }
        String password = randomStringUtil.generateRandomString(RANDOM_PASSWORD_STRENGTH);
        if (phoneNumber.isBlank()) {
            throw new RuntimeException("Phone can't be empty!");
        }
        String statusCode = sendRegisterOrLoginRequest(phoneNumber, password);
        String response = getResponseFromController(statusCode, phoneNumber, password);
        sendMarkedDownMessage(contact.getUserId(), response);
    }

    private void handleStop(Update update) {
        sendMessage(update.getMessage().getChatId(), STOPPED_SUCCESS);
    }

    private void handleUnknownCommand(Update update) {
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

    private String sendRegisterOrLoginRequest(String phoneNumber,
                                            String password) {
        String token = setActionTokenForCurrentRequest();
        String requestBody = formRequestBody(phoneNumber,
                    password, token);

        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(botToServerRequestUri))
                    .header(CONTENT_TYPE_HEADER, CONTENT_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(Duration.of(TIME_OUT, ChronoUnit.SECONDS))
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

    private String formTelegramResponse(String phoneNumber, String password) {
        return "Login: " + "`" + phoneNumber + "`" + System.lineSeparator()
                + "Password: " + "`" + password + "`" + System.lineSeparator()
                + "You can use your phone number and password to login\\." + System.lineSeparator()
                + "Just click on them to copy\\!"
                + System.lineSeparator()
                + "Don't forget to change your password when"
                + System.lineSeparator()
                + "already on the website to something "
                + System.lineSeparator()
                + "you will remember\\!";
    }

    private String formRequestBody(String phoneNumber,
                                   String password, String token) {
        return String.format("{\"userName\": \"%s\", "
                        + "\"password\": \"%s\", "
                        + "\"token\": \"%s\"}",
                phoneNumber,
                password,
                token);
    }

    private String getResponseFromController(String statusCode,
                                             String phoneNumber, String password) {
        if (statusCode.equals(CODE_200)) {
            return formTelegramResponse(phoneNumber, password);
        }
        return FAILED;
    }
}
