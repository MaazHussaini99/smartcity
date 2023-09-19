package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * Handles the button stuff for the transport tab
 */
public class TransportController {
    static ArrayList<Bus> buses = new ArrayList<>();

    /***
     * generate a list of buses
     * @throws SQLException
     */
    public static ArrayList<Bus> getBuses() throws SQLException {
        String busQ = "SELECT route_id, route_name, route_busNumber, route_desc FROM transport_route";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(busQ)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String routeName = resultSet.getString("route_name");
                String busNum = resultSet.getString("route_busNumber");
                String busDesc = resultSet.getString("route_desc");
                String routeMainId = resultSet.getString("route_id");
                //ArrayList<Stop> test = getStops(resultSet.getString("route_id"));
                buses.add(new Bus(busNum,routeName,busDesc, routeMainId));
            }
            return buses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /***
     * generate a hashmap of stations
     * @throws SQLException
     */
    public static ArrayList<Stop> getStops(String routeId) throws SQLException {
        ArrayList<Stop> stops = new ArrayList<>();

        // Use SQL joins with distinct aliases for tables
        String query = "SELECT ts.stop_name, tt.arrival_time, tt.departure_time " +
                "FROM transport_trip2 ttrip " +
                "JOIN transport_time2 tt ON ttrip.trip_id = tt.trip_id " +
                "JOIN transport_stop ts ON tt.stop_id = ts.stop_id " +
                "WHERE ttrip.route_id = ?";

        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SimpleStringProperty stopName = new SimpleStringProperty(resultSet.getString("stop_name"));
                SimpleStringProperty arrivalTime = new SimpleStringProperty(resultSet.getString("arrival_time"));
                SimpleStringProperty departureTime = new SimpleStringProperty(resultSet.getString("departure_time"));
                System.out.println(routeId + ": " + stopName);
                stops.add(new Stop(stopName, arrivalTime, departureTime));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return stops;
    }

    public static double getAccountBalance(int accountNumber) {

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
    public static int getAccountId(int userId) {
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

    public static boolean updateAccountBalance(int accountNumber, double amount) {
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

    public static void purchaseTicket(int qty){
        if((1.50 * qty) >= getAccountBalance(getAccountId(User.getInstance().getUserID()))){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Application Status");
            alert.setHeaderText(null);
            alert.setContentText("Sorry, Not Enough Funds!");
            alert.showAndWait();
        }
        else{
            double bankAmount = getAccountBalance(getAccountId(User.getInstance().getUserID())) - (1.50 * qty);
            updateAccountBalance(getAccountId(User.getInstance().getUserID()), bankAmount);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Application Status");
            alert.setHeaderText(null);
            alert.setContentText("Congratulations, you bought " + qty + " bus tickets for $" + (1.50 * qty));
            alert.showAndWait();
        }
    }
}