package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SignupController implements Initializable{

    @FXML
    private TextField emailIdTextField;
    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cityTextField;
    // State drop down
    @FXML
    private TextField zipCodeTextField;

    @FXML
    private Button signUp;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private ChoiceBox stateChoice;

    private int stateId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final String SELECT_QUERY = "SELECT * FROM states"; // Include stateid in the query
        String[] stateCodes = new String[50];
        Map<String, Integer> stateIdMap = new HashMap<>(); // To store the mapping of state code to state id

        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                int stateId = resultSet.getInt("state_id");
                String stateCode = resultSet.getString("state_code");
                stateCodes[i] = stateCode;
                i++;
                stateIdMap.put(stateCode, stateId);
            }
            System.out.println(stateCodes);

            // Create the ChoiceBox and set the default value to the first option
            ObservableList<String> stateList = FXCollections.observableArrayList(stateCodes);
            stateChoice.setItems(stateList);
            stateChoice.getSelectionModel().selectFirst();

            // Add an event listener to handle user selection
            stateChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    int selectedStateId = stateIdMap.get(newValue); // Get the stateid based on the selected state code
                    System.out.println("Selected State Code: " + newValue);
                    System.out.println("Corresponding State ID: " + selectedStateId);
                    stateId = selectedStateId;
                }
            });

        } catch (SQLException e) {
            // Handle the SQL exception
            printSQLException(e);
        }



    }
    public void BackToLogin(ActionEvent event) throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("logged-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 544, 400);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("Smart City - Sign up");
        stage.setScene(scene);
        stage.show();
    }

    public void signUp( ActionEvent event ) throws SQLException {

        Window owner = signUp.getScene().getWindow();

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
        if (zipCodeTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your Zip Code");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your Password");
            return;
        }
        if (confirmPassword.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please confirm your password");
            return;
        }
        if(!passwordField.getText().equals(confirmPassword.getText())){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Password do not match");
            return;
        }

        // Save user input from text fields
        String email = emailIdTextField.getText();
        String password = passwordField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String streetAddress = addressTextField.getText();
        String city = cityTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String zipCode = zipCodeTextField.getText();

        // Check if an account with the entered email already exists
        boolean flag = checkForEmail(email);
        System.out.println("Flag: "+flag);

        if (flag == true) {
            // Don't create account
            infoBox("An account with this email already exists.", null, "Failed");
            return;
        } else {
            // Create account
            addUser(email, password, firstName, lastName, streetAddress, city, stateId, phoneNumber, zipCode, 1);
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

    public static boolean addUser(String emailId, String userPassword, String fName, String lName, String streetAdd, String city, int state, String phoneNumber, String zipCode, int roleID) throws SQLException {

        final String INSERT_QUERY = "INSERT INTO user (first_name, last_name, user_streetaddress, user_city, user_zipcode, state_ID, user_email, user_password, user_phone, role_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {

            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setString(3, streetAdd);
            preparedStatement.setString(4, city);
            preparedStatement.setString(5, zipCode);
            preparedStatement.setInt(6, state);
            preparedStatement.setString(7, emailId);
            preparedStatement.setString(8, userPassword);
            preparedStatement.setString(9, phoneNumber);
            preparedStatement.setInt(10, roleID);

            int rowsInserted = preparedStatement.executeUpdate();

            // Check if any rows were inserted (1 row should be inserted for success)
            return rowsInserted == 1;

        } catch (SQLException e) {
            // Handle the SQL exception
            printSQLException(e);
            return false; // Return false to indicate the insertion failed
        }
    }

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
