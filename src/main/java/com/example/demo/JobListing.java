package com.example.demo;

<<<<<<< HEAD
=======
import javafx.scene.control.Alert;

>>>>>>> master
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

<<<<<<< HEAD
=======
    public static void applyJob(int jobID) {
        // Get the user ID from User.getInstance().getUserID()
        int userID = User.getInstance().getUserID();

        // Check if the entry already exists
        String selectSql = "SELECT * FROM jobapplication WHERE job_id = ? AND user_id = ?";
        boolean alreadyExists = false;

        try (Connection connection = DBConn.connectDB();
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            selectStatement.setInt(1, jobID);
            selectStatement.setInt(2, userID);

            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                alreadyExists = true; // Entry already exists
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (alreadyExists) {
            // Display an alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Application Status");
            alert.setHeaderText(null);
            alert.setContentText("You have already applied for this job.");

            alert.showAndWait();
        } else {
            // Insert the new application
            String insertSql = "INSERT INTO jobapplication (job_id, user_id) VALUES (?, ?)";

            try (Connection connection = DBConn.connectDB();
                 PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

                insertStatement.setInt(1, jobID);
                insertStatement.setInt(2, userID);

                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Application Status");
                    alert.setHeaderText(null);
                    alert.setContentText("Congratulations! You have applied for the job");
                    alert.showAndWait();
                } else {
                    System.out.println("Failed to submit application.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
>>>>>>> master

}
