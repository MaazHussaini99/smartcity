package com.example.demo;

public class HotelBookings {
    private int hotel_booking_id;
    private int hotel_id;
    private int user_id;
    private int account_id;

    public HotelBookings(int hotel_booking_id, int account_id, String check_in_time, String check_out_time, int hotel_total_cost) {
        this.hotel_booking_id = hotel_booking_id;
        this.account_id = account_id;
        this.check_in_time = check_in_time;
        this.check_out_time = check_out_time;
        this.hotel_total_cost = hotel_total_cost;
    }

    public int getHotel_booking_id() {
        return hotel_booking_id;
    }

    public void setHotel_booking_id(int hotel_booking_id) {
        this.hotel_booking_id = hotel_booking_id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
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

    public String getCheck_in_time() {
        return check_in_time;
    }

    public void setCheck_in_time(String check_in_time) {
        this.check_in_time = check_in_time;
    }

    public String getCheck_out_time() {
        return check_out_time;
    }

    public void setCheck_out_time(String check_out_time) {
        this.check_out_time = check_out_time;
    }

    public int getHotel_total_cost() {
        return hotel_total_cost;
    }

    public void setHotel_total_cost(int hotel_total_cost) {
        this.hotel_total_cost = hotel_total_cost;
    }
    @Override
    public String toString() {
        return hotel_booking_id + " "+account_id+" "+check_in_time+" "+check_out_time+" "+hotel_total_cost;
    }
    private String check_in_time;
    private String check_out_time;
    private int hotel_total_cost;
}
