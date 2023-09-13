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
            HotelBooking hb=new HotelBooking();
            int accountno=hb.getAccountId(uid);
            double balance=hb.getAccountBalance(accountno);
            int rowsAffected=0;
           if(balance>=totalCost) {
                rowsAffected = preparedStatement.executeUpdate();
           }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public double getAccountBalance(int accountNumber) {
        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT balance FROM bank_account WHERE account_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return a default value if there was an error or if the account doesn't exist
        return 0.0;
    }
    public int getAccountId(int userId) {
        int accountId = -1; // Default value in case of an error

        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT account_no FROM bank_account WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                accountId = resultSet.getInt("account_no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountId;
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
            HotelBooking hb=new HotelBooking();
            String emailId = HotelBooking.getInstance().getEmailId();
            HotelBooking c = new HotelBooking();
            int userId = c.getUserdetails(emailId);
            int accountno=hb.getAccountId(userId);
            double balance=hb.getAccountBalance(accountno);
            int rowsAffected=0;
            if(balance>=totalCost) {
                rowsAffected = preparedStatement.executeUpdate();
            }
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
            HotelBooking hb=new HotelBooking();
            String emailId = HotelBooking.getInstance().getEmailId();
            HotelBooking c = new HotelBooking();
            int userId = c.getUserdetails(emailId);
            int accountno=hb.getAccountId(userId);
            double balance=hb.getAccountBalance(accountno);
            int rowsAffected=0;
            if(balance>=totalCost) {
                rowsAffected = preparedStatement.executeUpdate();
            }
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

    public boolean updateAccountBalance(int accountNumber, double amount) {
        try (Connection connection = DBConn.connectDB()) {
            connection.setAutoCommit(false); // Disable auto-commit

            String sql = "UPDATE bank_account SET balance = ? WHERE account_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, accountNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit(); // Commit the transaction
                return true;
            } else {
                connection.rollback(); // Rollback if the update fails
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
