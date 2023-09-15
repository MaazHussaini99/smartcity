package com.example.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.Stage;

public class LoggedinController {

    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Button signUp;
    //private String userEmail;

    @FXML
    public void login(ActionEvent event) throws SQLException, IOException {

        Window owner = submitButton.getScene().getWindow();

        if (emailIdField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        String emailId = emailIdField.getText();
        String password = passwordField.getText();


        boolean flag = validate(emailId, password);
        System.out.println("Flag: "+flag);
        if (flag == true) {
            //userEmail = emailId;
            infoBox("Login Successful!", null, "Success");
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("landing-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 681);
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setTitle("Smart City");
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } else {
            infoBox("Please enter correct Email and Password", null, "Failed");
        }
    }
    @FXML
    public void goToSignUp(ActionEvent event) throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sign-up.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 535, 400);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("Smart City - Sign up");
        stage.setScene(scene);
        stage.show();
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public static boolean validate(String emailId, String password) throws SQLException {

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        final String SELECT_QUERY = "SELECT * FROM user WHERE user_email = ? and user_password = ?";
        try (Connection connection = DBConn.connectDB();

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, emailId);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            // 1 = user ID
            // 2 = first name
            // 3 = last name
            // 4 = street Add
            // 5 = city
            // 6 = zip
            // 7 = state
            // 8 = email
            // 9 = password
            // 10 = phone
            // 11 = role ID
            if (resultSet.next() == true) {

                PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM states WHERE state_ID = ?");
                preparedStatement2.setInt(1, Integer.parseInt(resultSet.getString(7)));
                ResultSet stateResultSet = preparedStatement2.executeQuery();

                if (stateResultSet.next() == true) {
                    // Save login data to User object
                    User user = User.initializeUser(Integer.parseInt(resultSet.getString(1)),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(6),
                            stateResultSet.getString(2),
                            resultSet.getString(8),
                            resultSet.getString(9),
                            resultSet.getString(10),
                            Integer.parseInt(resultSet.getString(11)));
                    System.out.println("Logged in!");
                    HotelBooking.getInstance().setEmailId(emailId);
                    return true;
                }
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}

