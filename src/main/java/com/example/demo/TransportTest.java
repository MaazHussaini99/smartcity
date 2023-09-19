package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * ignore pls
 */
public class TransportTest {
    String getBusNumber = "SELECT DISTINCT route_busNumber, route_name FROM transport_route;";
    String transportTrip = "SELECT * FROM transport_trip;";

        public TransportTest(){
            try {
                being();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    public void being() throws SQLException {
        Connection connection = DBConn.connectDB();
        //get route name + route_busNumber
        try (PreparedStatement preparedStatement = connection.prepareStatement(getBusNumber)) {
            String[] stateCodes = new String[50];
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                System.out.println(resultSet.getString("route_name"));
                System.out.println((resultSet.getString("route_busNumber")));
            }
            System.out.println(resultSet);
        }

    }
}
