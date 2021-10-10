package ru.praktika95.bot;

import org.apache.commons.collections.BidiMap;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class BotResponse {
    private SendPhoto sendPhoto;
    private SendMessage sendMessage;
    private ParsingData parsingData;
    private Event[] events;
    private Event selectedEvent;
    private boolean error;
    public final Map<String, Integer> map = Map.of(
            "main", 0,
            "date", 1,
            "category", 2,
            "events", 3,
            "event", 4,
            "period", 5
    );

    public BotResponse() {
        this.sendPhoto = new SendPhoto();
        this.sendMessage = new SendMessage();
        this.parsingData = new ParsingData();
        this.events = new Event[0];
        this.error = false;
        this.selectedEvent = new Event();
    }

    public void setMessage(String message) {
        sendMessage.setText(message);
        sendPhoto.setCaption(message);
    }

    public void setSendPhoto(int photoNumber){
        File f = new File("src\\main\\resources\\" + photoNumber + ".jpg");
        sendPhoto.setPhoto(new InputFile().setMedia(f));
    }

    public void setSendPhoto(String path){
        sendPhoto.setPhoto(new InputFile().setMedia(path));
    }

    public SendPhoto getSendPhoto()
    {
        return sendPhoto;
    }

    public void setChatId(String chatId)
    {
        sendMessage.setChatId(chatId);
        sendPhoto.setChatId(chatId);
    }

    public SendMessage getSendMessage()
    {
        return sendMessage;
    }

    public ParsingData getParsingData() {
        return parsingData;
    }

    public void setButtons(InlineKeyboardMarkup buttons) {
        this.sendMessage.setReplyMarkup(buttons);
        this.sendPhoto.setReplyMarkup(buttons);
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
        this.selectedEvent = event;
        String eventName = "Вы выбрали мероприятие:\n" + event.getName();
        String eventPlace = "\nОно состоится: " + event.getPlace();
        String eventTime = "\nДата: " + event.getDateTime();
        String eventPrice = "\nВходной билет стоит: "+ event.getPrice();
        this.setSendPhoto(event.getPhoto());
        this.sendMessage.setText(eventName + eventPlace + eventTime + eventPrice);
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMarkUp( InlineKeyboardMarkup inlineButtons ){
        sendMessage.setReplyMarkup(inlineButtons);
        sendPhoto.setReplyMarkup(inlineButtons);
    }
}
