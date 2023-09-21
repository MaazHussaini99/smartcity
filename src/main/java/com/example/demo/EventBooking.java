package com.example.demo;

import org.w3c.dom.events.Event;

public class EventBooking {
    private int event_booking_id;
    private int event_id;
    private int user_id;
    private int account_id;
    private double event_price;

    public EventBooking(){

    }

    public EventBooking(int event_booking_id, int event_id, int user_id, int account_id, double event_price) {
        this.event_booking_id = event_booking_id;
        this.event_id = event_id;
        this.user_id = user_id;
        this.account_id = account_id;
        this.event_price = event_price;
    }

    public EventBooking(int eventId, int userId, int accountId, double eventPrice) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.account_id = account_id;
        this.event_price = event_price;
    }

    public int getEvent_booking_id() {
        return event_booking_id;
    }

    public void setEvent_booking_id(int event_booking_id) {
        this.event_booking_id = event_booking_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public double getEvent_price() {
        return event_price;
    }

    public void setEvent_cost(int event_cost) {
        this.event_price = event_price;
    }
}
