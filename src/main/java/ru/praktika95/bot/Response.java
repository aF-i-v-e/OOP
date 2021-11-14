package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Response {

    private String text;
    private String chatId;
    private InputFile photoFile;
    private InlineKeyboardMarkup keyboard;
    private Event selectedEvent;
    private List<Event> events;
    private int startEventNumber;
    private int endEventNumber;
    private ParsingData parsingData;
    private boolean error;

    public final Map<String, Integer> map = Map.of(
            "main", 0,
            "date", 1,
            "category", 2,
            "events", 3,
            "event", 4,
            "period", 5
    );

    public Response() {
        this.parsingData = new ParsingData();
        this.events = new ArrayList<>();
        this.error = false;
        this.selectedEvent = new Event();
    }

    public Response(Response superEvent) {
        this.chatId = superEvent.getChatId();
        this.text = superEvent.getText();
        this.keyboard = superEvent.getKeyboard();
        this.photoFile = superEvent.getPhotoFile();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChatId() {
        return this.chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public InlineKeyboardMarkup getKeyboard() {
        return this.keyboard;
    }

    public void setKeyboard(InlineKeyboardMarkup keyboardMarkup) {
        this.keyboard = keyboardMarkup;
    }

    public InputFile getPhotoFile() {
        return this.photoFile;
    }

    public void setPhotoFile(int photoNumber) {
        File f = new File("src\\main\\resources\\" + photoNumber + ".jpg");
        this.photoFile = new InputFile().setMedia(f);
    }

    public void setPhotoFile(String path) {
        this.photoFile = new InputFile().setMedia(path);
    }

    public Event getSelectedEvent() {
        return this.selectedEvent;
    }

    public void setSelectedEvent(Event event) {
        this.selectedEvent = event;
        setPhotoFile(event.getPhoto());
        this.text = event.getEventFullDescription();
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getStartEventNumber() {
        return startEventNumber;
    }

    public void setStartEventNumber(int startEvent) {
        this.startEventNumber = startEvent;
    }

    public int getEndEventNumber() {
        return endEventNumber;
    }

    public void setEndEventNumber(int endEvent) {
        this.endEventNumber = endEvent;
    }

    public void setNullEvents() {
        this.startEventNumber = 0;
        this.events = new ArrayList<>();
    }

    public void createButtons(String typeButtons, String number, boolean isEnd, String url) {
        List<List<InlineKeyboardButton>> rowList = ReplyMarkup.findButtons(typeButtons).handler(typeButtons, number, isEnd, url);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        this.keyboard = inlineKeyboardMarkup;
    }

    public void setPeriod(DatePeriod period) {
        parsingData.setPeriod(period);
    }

    public void setCategory(String codeCategory) {
        this.parsingData.setCategory(codeCategory);
    }

    public ParsingData getParsingData() {
        return this.parsingData;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setNull() {
        this.text = "";
        this.photoFile = null;
        this.keyboard = null;
    }
}
