package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobListing {

    public static void getAllJobs(){
        String sql = "SELECT * FROM jobs";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
//                System.out.println(resultSet.getString(2));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
