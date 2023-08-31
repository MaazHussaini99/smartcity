package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private TextField emailIdTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField middleInitialTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cityTextField;
    // State drop down
    @FXML
    private TextField zipCodeTextField;

    @FXML
    private Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Make submitButton clickable and have it run the signUp method
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                signUp(actionEvent);
                // TODO: Something to send user to home page
            }
        });
    }


    public void signUp( ActionEvent event ) {


        Window owner = submitButton.getScene().getWindow();

        // Email validation
        if (emailIdTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email");
            return;
        }
        if (phoneNumberTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your phone number");
            return;
        }
        // Password validation
        if (firstNameTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your first name");
            return;
        }
        if (lastNameTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your last name");
            return;
        }
        if (middleInitialTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your middle initial");
            return;
        }
        if (addressTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your Street Address");
            return;
        }
        if (cityTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the City");
            return;
        }
        if (zipCodeTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your Zip Code");
            return;
        }

        // Save user input from text fields
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String middleInitial = middleInitialTextField.getText();
        String email = emailIdTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String streetAddress = addressTextField.getText();
        String city = cityTextField.getText();
        String zipCode = zipCodeTextField.getText();

        // Check if an account with the entered email already exists
        boolean flag = checkForEmail(email);
        System.out.println("Flag: "+flag);
        if (flag == true) {
            // Don't create account
            infoBox("An account with this email already exists.", null, "Failed");
        } else {
            // Create account
            infoBox("Registration Successful", null, "Success");

            // TODO: Create new account and add it to the database
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
