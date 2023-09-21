package com.example.demo;

public class Event extends EventBooking {
    private int eventId;
    private String eventName;
    private String eventDate;
    private String eventLocation;
    private double eventPrice;
    public Event() {

    }
    public Event(int eventId, String eventName,String eventLocation, String eventDate, double eventPrice) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventPrice = eventPrice;
    }

    public Event(String eventName, String eventLocation, String eventDate, double eventPrice) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventPrice = eventPrice;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }
    public int getEventId() {
        return eventId;
    }
    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public double getEventPrice() {
        return eventPrice;
    }
}

