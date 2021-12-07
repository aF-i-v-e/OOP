package ru.praktika95.bot.hibernateTest;

import ru.praktika95.bot.handle.response.Event;

import javax.persistence.*;

@Entity
@Table(name = "test")
public class TestUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String chatId;

    @Column
    private String eventDateNotice;

    @Column
    private String eventUrl;

    public TestUser() {

    }

    public TestUser(String chatId, String eventDateNotice, String eventUrl) {
        this.chatId = chatId;
        this.eventDateNotice = eventDateNotice;
        this.eventUrl = eventUrl;
    }

    public Event getEvent() {
        Event event = new Event(null, null, null, null, null, null, eventUrl);
        event.setDateNotice(eventDateNotice);
        return event;
    }

    public Integer getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }

    public String getEventDateNotice() {
        return eventDateNotice;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chatId == null) ? 0 : chatId.hashCode());
        result = prime * result + ((eventDateNotice == null) ? 0 : eventDateNotice.hashCode());
        result = prime * result + ((eventUrl == null) ? 0 : eventUrl.hashCode());
        return result;
    }

    @Override
    public String toString() {
        Event event = getEvent();
        return event.getEventFullDescription(false);
    }
}
