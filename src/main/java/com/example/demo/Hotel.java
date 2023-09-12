package com.example.demo;

public class Hotel {
    private int hotelId;
    private String hotelName;
    private String hotelLocation;
    private int hotelPrice;
    private int hotelRoomNumber;
    private int hotelAvailability;

    public Hotel(int hotelId, String hotelName, String hotelLocation, int hotelPrice, int hotelRoomNumber, int hotelAvailability) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelLocation = hotelLocation;
        this.hotelPrice = hotelPrice;
        this.hotelRoomNumber = hotelRoomNumber;
        this.hotelAvailability = hotelAvailability;
    }

    public int getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getHotelLocation() {
        return hotelLocation;
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

    public int getHotelRoomNumber() {
        return hotelRoomNumber;
    }

    public int getHotelAvailability() {
        return hotelAvailability;
    }

    @Override
    public String toString() {
        return hotelName + " "+hotelLocation+" "+hotelPrice+" "+hotelAvailability;
    }
}
