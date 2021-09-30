package ru.praktika95.bot;

public class BotResponse {
    private String message;
    private ParsingData parsingData;
    private Event[] events;
    private boolean error;

    public BotResponse() {
        this.message = null;
        this.parsingData = new ParsingData();
        this.events = new Event[0];
        this.error = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        message = msg;
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
}
