package ru.praktika95.bot;

public class Event {
    private String photo;
    private String name;
    private String dateTime;
    private String place;
    private String price;

    public Event() { }

    public Event(String photo, String name, String dateTime, String place, String price) {
        this.photo = photo;
        this.name = name;
        this.dateTime = dateTime;
        this.place = place;
        this.price = price;
    }

    public String getPhoto(){
        return photo;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDateTime(){
        return dateTime;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

    public String getPlace(){
        return place;
    }

    public void setPlace(String place){
        this.place = place;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String price){
        this.price = price;
    }
}
