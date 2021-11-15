package ru.praktika95.bot.hibernate;
import ru.praktika95.bot.*;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "date_notice")
    private String eventDateNotice;

    @Column(name = "photo_event")
    private String eventPhoto;

    @Column(name = "name_event")
    private String eventName;

    @Column(name = "date_event")
    private String eventDateTime;

    @Column(name = "place_event")
    private String eventPlace;

    @Column(name = "price_event")
    private String eventPrice;

    @Column(name = "url_event")
    private String eventUrl;

    public Users() {

    }

    public Users(String chatId, Event event) {
        this.chatId = chatId;
        this.eventPhoto = event.getPhoto();
        this.eventName = event.getName();
        this.eventDateTime = event.getDateTime();
        this.eventPlace = event.getPlace();
        this.eventPrice = event.getPrice();
        this.eventUrl = event.getUrl();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getEventDateNotice() {
        return eventDateNotice;
    }

    public void setEventDateNotice(String eventDateNotice) {
        this.eventDateNotice = eventDateNotice;
    }

    public String getEventPhoto() {
        return eventPhoto;
    }

    public void setEventPhoto(String eventPhoto) {
        this.eventPhoto = eventPhoto;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(String eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventPhoto == null) ? 0 : eventPhoto.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((eventName == null) ? 0 : eventName.hashCode());
        result = prime * result + ((eventPlace == null) ? 0 : eventPlace.hashCode());
        result = prime * result + ((eventPrice == null) ? 0 : eventPrice.hashCode());
        result = prime * result + ((eventDateTime == null) ? 0 : eventDateTime.hashCode());
        result = prime * result + ((eventUrl == null) ? 0 : eventUrl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Users other = (Users) obj;
        if (chatId == null) {
            if (other.chatId != null)
                return false;
        } else if (!chatId.equals(other.chatId))
            return false;
        if (eventDateNotice == null) {
            if (other.eventDateNotice != null)
                return false;
        } else if (!eventDateNotice.equals(other.eventDateNotice))
            return false;
        if (eventPhoto == null) {
            if (other.eventPhoto != null)
                return false;
        } else if (!eventPhoto.equals(other.eventPhoto))
            return false;
        if (eventName == null) {
            if (other.eventName != null)
                return false;
        } else if (!eventName.equals(other.eventName))
            return false;
        if (eventDateTime == null) {
            if (other.eventDateTime != null)
                return false;
        } else if (!eventDateTime.equals(other.eventDateTime))
            return false;
        if (eventPlace == null) {
            if (other.eventPlace != null)
                return false;
        } else if (!eventPlace.equals(other.eventPlace))
            return false;
        if (eventPrice == null) {
            if (other.eventPrice != null)
                return false;
        } else if (!eventPrice.equals(other.eventPrice))
            return false;
        if (eventUrl == null) {
            if (other.eventUrl != null)
                return false;
        } else if (!eventUrl.equals(other.eventUrl))
            return false;
        return true;
    }

    @Override
    public String toString() {
        Event event = new Event(eventPhoto, eventName, eventDateTime, eventPlace, eventPrice, eventUrl);
        return event.getEventFullDescription();
    }
}
