package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class BotRequest {
    String chatId;
    String inputText;
    String command;
    int status;
    List<List<InlineKeyboardButton>> buttons;

    public BotRequest(Update update){
        chatId = update.getMessage().getChatId().toString();
        inputText = update.getMessage().getText();
        buttons = new ArrayList<>();
    }
}
