package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.function.Function;

public class BotRequest {
    String chatId;
    String typeButtons;
    String button;
    String botCommand;
    Map<String,Integer> map;

    public BotRequest(Update update){
        chatId = update.getMessage().getChatId().toString();
        map = new HashMap<String, Integer>();
    }
}
