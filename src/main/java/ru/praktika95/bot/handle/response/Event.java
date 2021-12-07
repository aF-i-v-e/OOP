package ru.praktika95.bot.handle.response;

import ru.praktika95.bot.handle.format.FormatDate;

public class Event {
    private String photo;
    private String name;
    private String date;
    private String time;
    private String place;
    private String price;
    private String url;
    private String dateNotice;
    private Integer idBD;

    public Event() { }

    public Event(String photo, String name, String date, String time, String place, String price, String url) {
        this.photo = photo;
        this.name = name;
        this.date = date;
        this.time = time;
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

    public String getDate(){
        return date;
    }

    public String getDateTime() {
        return FormatDate.userFormatDate(date) + ", " + time;
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

    public String getEventFullDescription(Boolean needToFormate) {
        String eventName = "Вы выбрали мероприятие:\n" + name;
        String eventPlace = "\nОно состоится: " + place;
        String eventTime = needToFormate
                ? "\nДата: " + FormatDate.userFormatDate(date) + ", "+ time
                : "\nДата: " + date + ", " + time + "\nДата уведомления: " + dateNotice;
        String eventPrice = "\nВходной билет стоит: "+ price;
        return eventName + eventPlace + eventTime + eventPrice;
    }

    public String getEventBriefDescription(Boolean needToFormatDate) {
        return needToFormatDate
                ? "\n✧ Мероприятие: " + name + "\n✧ Дата: " + FormatDate.userFormatDate(date) + ", " + time
                : "\n✧ Мероприятие: " + name + "\n✧ Дата: " + FormatDate.userFormatDate(date) + ", " + time + "\n✧ Дата уведомления: " + dateNotice;
    }

    public String getEventNotification(String period) {
        String eventName = "Вы выбрали: \""  + name + "\"";
        String eventDate = "\nОно состоится: " + FormatDate.userFormatDate(date) + ", "+ time;
        String notification = "\nEkbEventBot оповестит Вас за " + period + " о мероприятии, которое Вы выбрали";
        return eventName + eventDate + notification;
    }

    public String getNotifyMessage() {
        String notificationMsg = "Уведомление о посещении мероприятия! ";
        String eventDescription = getEventBriefDescription(false);
        String gratitudeMsg = "\nСпасибо за пользование EkbEventBot!";
        String wishMsg = "\nЖелаем Вам приятно провести время на мероприятии!";
        return  notificationMsg + eventDescription + gratitudeMsg + wishMsg;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
