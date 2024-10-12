package com.example.budgetingapp.telegramregistrationbot;

import static com.example.budgetingapp.constants.security.SecurityConstants.RANDOM_PASSWORD_STRENGTH;
import static com.example.budgetingapp.constants.security.SecurityConstants.TOKEN;

import com.example.budgetingapp.dtos.user.request.TelegramAuthenticationRequestDto;
import com.example.budgetingapp.security.RandomStringUtil;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RandomStringUtil randomStringUtil;

    private boolean isBotActive;

    public BudgetAppBot(@Autowired RandomStringUtil randomStringUtil) {
        super(TOKEN);
        getBotUsername();
        this.randomStringUtil = randomStringUtil;
    }

    @Override
    public String getBotUsername() {
        return "BudgetApplicationBot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()
                && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            switch (command) {
                case "/start" -> {
                    isBotActive = true;
                    requestContact(update);
                }
                case "/stop" -> {
                    if (isBotActive) {
                        isBotActive = false;
                        handleStop(update);
                    }
                }
                default -> handleUnknownCommand(update);

            }
        } else if (update.hasMessage()
                && update.getMessage().hasContact()) {
            handleContact(update.getMessage().getContact());
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
        message.setText("Please share your contact information:");
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
        sendRegisterOrLoginRequest(new TelegramAuthenticationRequestDto(firstName,
                lastName,
                phoneNumber,
                password));
        String response = "First name: " + firstName + System.lineSeparator()
                + "Last name: " + lastName + System.lineSeparator()
                + "Phone number: " + phoneNumber + System.lineSeparator()
                + "Password: " + password + System.lineSeparator()
                + "You can use your phoneNumber number " + System.lineSeparator()
                + "and password to login" + System.lineSeparator();
        sendMessage(contact.getUserId(), response);
    }

    private void handleStop(Update update) {
        sendMessage(update.getMessage().getChatId(), "The bot has been stopped!");
    }

    private void handleUnknownCommand(Update update) {
        if (!isBotActive) {
            return;
        }
        sendMessage(update.getMessage().getChatId(),
                "Unknown command. Please use /start, or /stop.");
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

    private void sendRegisterOrLoginRequest(TelegramAuthenticationRequestDto requestDto) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String requestBody = String.format("{\"firstName\": \"%s\", "
                    + "\"lastName\": \"%s\", "
                    + "\"phoneNumber\": \"%s\", "
                    + "\"password\": \"%s\"}",
                    requestDto.firstName(),
                    requestDto.lastName(),
                    requestDto.phoneNumber(),
                    requestDto.password());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/auth/telegramauth"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
