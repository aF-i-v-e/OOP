package ru.praktika95.bot;

public class Event {
    private String photo;
    private String name;
    private String dateTime;
    private String place;
    private String price;
    private String url;
    private String dateNotice;
    private Integer idBD;

    public Event() { }

    public Event(String photo, String name, String dateTime, String place, String price, String url) {
        this.photo = photo;
        this.name = name;
        this.dateTime = dateTime;
        this.place = place;
        this.price = price;
        this.url = url;
    }

    public String getPhoto(){
        return photo;
    }

    public String getName(){
        return name;
    }

    public String getDateTime(){
        return dateTime;
    }

    public String getPlace(){
        return place;
    }

    public String getPrice(){
        return price;
    }

    public String getUrl() {
        return url;
    }

    public String getDateNotice() {
        return dateNotice;
    }

    public void setDateNotice(String dateNotice) {
        this.dateNotice = dateNotice;
    }

    public Integer getIdBD() {
        return idBD;
    }

    public void setIdBD(Integer idBD) {
        this.idBD = idBD;
    }

    public String getEventFullDescription() {
        String eventName = "Вы выбрали мероприятие:\n" + name;
        String eventPlace = "\nОно состоится: " + place;
        String eventTime = "\nДата: " + dateTime;
        String eventPrice = "\nВходной билет стоит: "+ price;
        String resultText = eventName + eventPlace + eventTime + eventPrice;
        return resultText;
    }

    public String getEventBriefDescription() {
        return "\n✧ Мероприятие: " + name + "\n✧ Дата: " + dateTime;
    }

    public String getEventNotification(String period) {
        String eventName = "Вы выбрали: \""  + name + "\"";
        String eventDate = "\nОно состоится: " + dateTime;
        String notification = "\nEkbEventBot оповестит Вас за " + period + " о мероприятии, которое Вы выбрали";
        String resultText = eventName + eventDate + notification;
        return resultText;
    }
}
