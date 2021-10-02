package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class BotResponse {
    private SendMessage sendMessage;
    private ParsingData parsingData;
    private Event[] events;
    private boolean error;
    private InlineKeyboardMarkup buttonMarkup;

    public BotResponse() {
        this.sendMessage = new SendMessage();
        this.parsingData = new ParsingData();
        this.events = new Event[0];
        this.error = false;
        this.buttonMarkup=new InlineKeyboardMarkup();
    }

    public void setMessage(String message)
    {
        sendMessage.setText(message);
    }

    public void setChatId(String chatId)
    {
        sendMessage.setChatId(chatId);
    }

    public SendMessage getSendMessage()
    {
        return sendMessage;
    }

    public ParsingData getParsingData() {
        return parsingData;
    }

    public void setCategory(int codeCategory) {
        parsingData.setCategory(codeCategory);
    }

    public void setPeriod(DatePeriod period) {
        parsingData.setPeriod(period);
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setButtonMarkup(List<List<InlineKeyboardButton>> buttons)
    {
        buttonMarkup.setKeyboard(buttons);
    }

    public InlineKeyboardMarkup getButtonMarkup()
    {
        return  buttonMarkup;
    }
}
