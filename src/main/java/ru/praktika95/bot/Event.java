package ru.praktika95.bot;

public class Event {
    private String photo;
    private String name;
    private String date;
    private String time;
    private String place;
    private String price;
    private String url;

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

    public String getTime(){
        return time;
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
