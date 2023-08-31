package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignupController {

    @FXML
    private TextField firstNameTxtBox;
    @FXML
    private TextField lastNameTxtBox;
    @FXML
    private TextField middleInitialTxtBox;
    @FXML
    private TextField emailIdFieldTxtBox;
    @FXML
    private TextField phoneNumberTxtBox;
    @FXML
    private TextField addressTxtBox;
    @FXML
    private TextField cityTxtBox;
    // State drop down
    @FXML
    private TextField zipCodeTxtBox;

    @FXML
    private Button submitButton;

    public void signUp( ActionEvent event ) throws SQLException {


        Window owner = submitButton.getScene().getWindow();

        // Check if any fields are blank
        if (firstNameTxtBox.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your first name");
            return;
        }
        if (lastNameTxtBox.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your last name");
            return;
        }
        if (middleInitialTxtBox.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your middle initial");
            return;
        }
        if (emailIdFieldTxtBox.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email");
            return;
        }
        if (phoneNumberTxtBox.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your phone number");
            return;
        }
        if (addressTxtBox.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your Street Address");
            return;
        }
        if (cityTxtBox.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the City");
            return;
        }
        if (zipCodeTxtBox.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your Zip Code");
            return;
        }

        // Save user input from text fields
        String firstName = firstNameTxtBox.getText();
        String lastName = lastNameTxtBox.getText();
        String middleInitial = middleInitialTxtBox.getText();
        String email = emailIdFieldTxtBox.getText();
        String phoneNumber = phoneNumberTxtBox.getText();
        String streetAddress = addressTxtBox.getText();
        String city = cityTxtBox.getText();
        String zipCode = zipCodeTxtBox.getText();

        // Check if an account with the entered email already exists
        boolean flag = checkForEmail(email);
        System.out.println("Flag: "+flag);
        if (flag == true) {
            // Don't create account
            infoBox("An account with this email already exists.", null, "Failed");
        } else {
            // Create account
            infoBox("Registration Successful", null, "Success");
        }

    } // End of signUp

    public static boolean checkForEmail(String emailId) throws SQLException {

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        final String SELECT_QUERY = "SELECT * FROM user WHERE user_email = ?";
        try (Connection connection = DBConn.connectDB();

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, emailId);

            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() == true) {
                System.out.println("An account with this email already exists.");
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    } // End of checkForEmail

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    } // End of infoBox

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    } // End of showAlert

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
    } // End of printSQLException
}
