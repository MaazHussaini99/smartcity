package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
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
}