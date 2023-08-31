package com.example.demo;
import java.sql.*;

public class HotelBooking {
    private int hotelBookId;
    private int hotelId;
    private int uid;
    private int accountId;
    private String checkInDate;
    private String checkOutDate;

    public HotelBooking(int hotelBookId, int hotelId, int uid, int accountId, String checkInDate, String checkOutDate) {
        this.hotelBookId = hotelBookId;
        this.hotelId = hotelId;
        this.uid = uid;
        this.accountId = accountId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    // Method to create a new hotel booking
    public boolean createBooking(int hotelBookId, int hotelId, int uid, int accountId, String checkInDate, String checkOutDate, double totalCost) {
        String sql = "INSERT INTO hotel_booking (hotel_booking_id, hotel_id, user_id, account_id, check_in_time, check_out_time, hotel_total_cost) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, hotelBookId);
            preparedStatement.setInt(2, hotelId);
            preparedStatement.setInt(3, uid);
            preparedStatement.setInt(4, accountId);
            preparedStatement.setString(5, checkInDate);
            preparedStatement.setString(6, checkOutDate);
            preparedStatement.setDouble(7, totalCost);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to edit an existing hotel booking
    public boolean editBooking(String newCheckIn, String newCheckOut,int hotelBookId) {
        String sql = "UPDATE hotel_booking SET check_in_time = ?, check_out_time = ? WHERE hotel_booking_id = ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1,newCheckIn);
            preparedStatement.setString(2, newCheckOut);
            preparedStatement.setInt(3, hotelBookId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to cancel an existing hotel booking
    public boolean cancelBooking(int hotelBookId) {
        String sql = "DELETE FROM hotel_booking WHERE hotel_booking_id = ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, hotelBookId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to extend an existing hotel booking
    public boolean extendBooking(String newCheckOut, int hotelBookId) {
        String sql = "UPDATE hotel_booking SET check_out_time = ? WHERE hotel_booking_id = ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newCheckOut);
            preparedStatement.setInt(2, hotelBookId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to retrieve hotel booking details for a specific user
    public ResultSet getBookingDetails(int userId) {
        String sql = "SELECT * FROM hotel_booking WHERE user_id = ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
