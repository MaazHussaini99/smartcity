package com.example.demo;
import java.sql.*;

public class HotelBooking {
    private int hotelBookId;
    private int hotelId;
    private int uid;
    private int accountId;
    private String checkInDate;
    private String checkOutDate;
   private static int userId;
   private String emailId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    private static HotelBooking instance;
    public static HotelBooking getInstance() {
        if (instance == null) {
            instance = new HotelBooking();
        }
        return instance;
    }
    public HotelBooking(){

    }
    public HotelBooking(int hotelBookId, int hotelId, int uid, int accountId, String checkInDate, String checkOutDate) {
        this.hotelBookId = hotelBookId;
        this.hotelId = hotelId;
        this.uid = uid;
        this.accountId = accountId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    // Method to create a new hotel booking
    public boolean createBooking(int hotelId, int uid, int accountId, String checkInDate, String checkOutDate, double totalCost) {
        String sql = "INSERT INTO hotel_booking (hotel_id, user_id, account_id, check_in_time, check_out_time, hotel_total_cost) VALUES ( ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, hotelId);
            preparedStatement.setInt(2, uid);
            preparedStatement.setInt(3, accountId);
            preparedStatement.setString(4, checkInDate);
            preparedStatement.setString(5, checkOutDate);
            preparedStatement.setDouble(6, totalCost);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to edit an existing hotel booking
    public boolean editBooking(String newCheckIn, String newCheckOut,int hotelBookId, int totalCost) {
        String sql = "UPDATE hotel_booking SET check_in_time = ?, check_out_time = ?, hotel_total_cost=? WHERE hotel_booking_id = ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1,newCheckIn);
            preparedStatement.setString(2, newCheckOut);
            preparedStatement.setInt(4, hotelBookId);
            preparedStatement.setInt(3, totalCost);

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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to extend an existing hotel booking
    public boolean extendBooking(String newCheckIn,String newCheckOut, int hotelBookId, int totalCost) {
        String sql = "UPDATE hotel_booking SET check_in_time=?, check_out_time = ?, hotel_total_cost=?  WHERE hotel_booking_id = ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newCheckIn);
            preparedStatement.setString(2, newCheckOut);
            preparedStatement.setInt(4, hotelBookId);
            preparedStatement.setInt(3, totalCost);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
           // System.out.println(e.printStackTrace());
            return false;
        }
    }
    public int getUserdetails(String emailId){
        System.out.println(emailId);
        String sql = "SELECT uid FROM user where user_email= ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,emailId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
               final int userId=resultSet.getInt("uid");
                //HotelBooking.getInstance().setUserId(userId);
                this.userId=userId;
                int c=HotelBooking.getInstance().getUserId();
                return userId;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;

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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
