package ru.praktika95.bot.hibernate;
import ru.praktika95.bot.*;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

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

    public User() {

    }

    public User(String chatId, Event event) {
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

    private String[] getDateAndTime() {
        return eventDateTime.split(", ");
    }

    public String getEventDate() {
        return getDateAndTime()[0];
    }

    public String getEventTime() {
        return getDateAndTime()[1];
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj; //может быть из таблицы БД, а может быть созданный нами
        if (this.hashCode() == other.hashCode()) {
            if (other.id == null) {
                return chatId.equals(other.chatId) &&
                        eventDateNotice.equals(other.eventDateNotice) && //даты, когда надо оповестить равны
                        eventUrl.equals(other.eventUrl);
            }
            return id.equals(other.id); // иначе гарантируем уникальность по id, разные id - разные события
        }
        return false;
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
        Event event = new Event(eventPhoto, eventName, getEventDate(), getEventTime(), eventPlace, eventPrice, eventUrl);
        return event.getEventFullDescription(false);
    }
}
