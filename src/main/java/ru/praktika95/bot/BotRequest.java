package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.function.Function;

public class BotRequest {
    private String chatId;
    private String typeButtons;
    private String botCommand;
    private String selectedEvent;
    public final Map<String,Integer> map = Map.of(
            "main", 0,
            "date", 1,
            "category", 2,
            "events", 3,
            "event", 4,
            "period", 5
    );

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

    public void setChatId(String chatId){
        this.chatId=chatId;
    }

    public String getChatId(){
        return chatId;
    }

    public String getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(String selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
}
