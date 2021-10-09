package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.function.Function;

public class BotRequest {
    String chatId;
    String typeButtons;
    String botCommand;

    public BotRequest(Update update){
        if (update.hasCallbackQuery())
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        else
            chatId = update.getMessage().getChatId().toString();
    }

    public String getTypeButtons() {
        return typeButtons;
    }

    public void setTypeButtons(String typeButtons) {
        this.typeButtons = typeButtons;
    }

    public String getBotCommand() {
        return botCommand;
    }

    public void setBotCommand(String botCommand) {
        this.botCommand = botCommand;
    }
}
