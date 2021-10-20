package ru.praktika95.bot;

public class Event {
    private String photo;
    private String name;
    private String dateTime;
    private String place;
    private String price;
    private String url;

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
}
