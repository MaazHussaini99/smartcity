package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

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
    private Button submitButton;

    public void signUp( ActionEvent event ) throws SQLException {


        Window owner = submitButton.getScene().getWindow();


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

        String firstName = firstNameTxtBox.getText();
        String lastName = lastNameTxtBox.getText();
        String middleInitial = middleInitialTxtBox.getText();
        String email = emailIdFieldTxtBox.getText();
        String phoneNumber = phoneNumberTxtBox.getText();
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
