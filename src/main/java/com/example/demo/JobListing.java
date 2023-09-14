package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobListing {

    static List<Job> jobs = new ArrayList<>();
    public static List<Job> getAllJobs(){
        String sql = "SELECT * FROM jobs";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                jobs.add(new Job(resultSet.getString(2), resultSet.getInt(1),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5)));
            }
        return jobs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
