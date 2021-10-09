package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.function.Function;

public class BotRequest {
    String chatId;
    String typeButtons;
    String botCommand;
    public final Map<String,Integer> map = Map.of(
            "main", 0,
            "data", 1,
            "category", 2,
            "events", 3,
            "event", 4,
            "period", 5
    );

    public BotRequest(Update update){
        chatId = update.getMessage().getChatId().toString();
    }
}
