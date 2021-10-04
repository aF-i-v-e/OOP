package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class BotResponse {
    private SendMessage sendMessage;
    private ParsingData parsingData;
    private Event[] events;
    private Event selectedEvent;
    private boolean error;
    private InlineKeyboardMarkup buttonMarkup;

    public BotResponse() {
        this.sendMessage = new SendMessage();
        this.parsingData = new ParsingData();
        this.events = new Event[0];
        this.error = false;
        this.buttonMarkup=new InlineKeyboardMarkup();
        this.selectedEvent =new Event();
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

    public void setSelectedEvent(Event event)
    {
        selectedEvent = event;
        String textPart1=sendMessage.getText();
        String textPart2="\n Мероприятие:"+event.getName()+"\n Состоится:"+event.getPlace()+"\n Цена: "+ event.getPrice();
        sendMessage.setText(textPart1+textPart2);

    }
    public Event getSelectedEvent()
    {
        return selectedEvent;
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
