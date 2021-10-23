package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

public class BotRequest {
    private String chatId;
    private String typeButtons;
    private String botCommand;
    private String selectedEvent;
    final int BasicArrayLength  = 2;
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
            setBotRequestFromCallbackQuery(update);
        else
            chatId = update.getMessage().getChatId().toString();
    }

    public void setBotRequestFromCallbackQuery(Update update){
        chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String[] callbackData = callbackQuery.getData().split(" ");
        this.setTypeButtons(callbackData[0]);
        this.setBotCommand(callbackData[1]);
        if (callbackData.length > BasicArrayLength)
            this.setSelectedEvent(callbackData[2]);
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
