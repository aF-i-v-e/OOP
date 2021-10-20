package ru.praktika95.bot;

import org.checkerframework.checker.units.qual.A;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotResponse {
    private SendPhoto sendPhoto;
    private SendMessage sendMessage;
    private ParsingData parsingData;
    private List<Event> events;
    private Event selectedEvent;
    private int startEvent;
    private int endEvent;
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
        this.events = new ArrayList<>();
        this.error = false;
        this.selectedEvent = new Event();
    }

    public BotResponse(BotResponse superBot){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(superBot.getSendPhoto().getPhoto());
        sendPhoto.setCaption(superBot.getSendPhoto().getCaption());
        sendPhoto.setChatId(superBot.getSendPhoto().getChatId());
        sendPhoto.setReplyMarkup(superBot.getSendMessage().getReplyMarkup());
        this.sendPhoto =  sendPhoto;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(superBot.getSendMessage().getText());
        sendMessage.setChatId(superBot.getSendMessage().getText());
        sendMessage.setReplyMarkup(superBot.getSendMessage().getReplyMarkup());
        this.sendMessage = sendMessage;
    }

    public void setMessage(String message) {
        sendMessage.setText(message);
        sendPhoto.setCaption(message);
    }

    public void setSendPhoto(int photoNumber) {
        File f = new File("src\\main\\resources\\" + photoNumber + ".jpg");
        sendPhoto.setPhoto(new InputFile().setMedia(f));
    }

    public void setSendPhoto(String path) {
        sendPhoto.setPhoto(new InputFile().setMedia(path));
    }

    public void setNull() {
        SendPhoto photo = new SendPhoto();
        photo.setCaption("");
        photo.setChatId(this.getSendPhoto().getChatId());
        SendMessage message = new SendMessage();
        message.setText("");
        message.setChatId(this.getSendMessage().getChatId());
        this.sendPhoto = photo;
        sendMessage = message;
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

    public void setCategory(String codeCategory) {
        parsingData.setCategory(codeCategory);
    }

    public void setPeriod(DatePeriod period) {
        parsingData.setPeriod(period);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setNullEvents() {
        this.startEvent = 0;
        this.events = new ArrayList<>();
    }

    public void setSelectedEvent(Event event)
    {
        this.selectedEvent = event;
        String eventName = "Вы выбрали мероприятие:\n" + event.getName();
        String eventPlace = "\nОно состоится: " + event.getPlace();
        String eventTime = "\nДата: " + event.getDateTime();
        String eventPrice = "\nВходной билет стоит: "+ event.getPrice();
        this.setSendPhoto(event.getPhoto());
        sendMessage.setText(eventName + eventPlace + eventTime + eventPrice);
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(int startEvent) {
        this.startEvent = startEvent;
    }

    public int getEndEvent() {
        return endEvent;
    }

    public void setEndEvent(int endEvent) {
        this.endEvent = endEvent;
    }

    public void createButtons(String typeButtons, String number, boolean isEnd) {
        List<List<InlineKeyboardButton>> rowList = ReplyMarkup.findButtons(typeButtons).handler(typeButtons, number, isEnd);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
    }
}
