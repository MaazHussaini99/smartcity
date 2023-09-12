package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankController {

    @FXML
    private VBox bankView;

    @FXML
    private TextField newBankNameField;
    @FXML
    private ListView<String> bankListView;

    @FXML
    private TextField updateBankNameField;

    @FXML
    private VBox deleteAndUpdateContainer;
    // Add a flag to track whether to show the "Delete" and "Update" sections
    private boolean showDeleteAndUpdate = false;
    public ObservableList<String> bankNames = FXCollections.observableArrayList();

    public void initialize() {
        bankListView.setCellFactory(TextFieldListCell.forListView());

        // Load bank list when initializing
        loadBankList();
    }
    @FXML
    public void bankSelected() {
        String selectedBank = bankListView.getSelectionModel().getSelectedItem();
        if (selectedBank != null) {
            // Show the Delete and Update sections
            showDeleteAndUpdateSections(true);
        } else {
            // Hide the Delete and Update sections if no bank is selected
            showDeleteAndUpdateSections(false);
        }
    }
    @FXML
    public void createNewBank() {
        String newBankName = newBankNameField.getText();
        if (!newBankName.isEmpty()) {
            try (Connection connection = DBConn.connectDB()) {
                String sql = "INSERT INTO Bank (bank_name) VALUES (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newBankName);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showSuccessMessage("Bank Created Successfully!");
                    newBankNameField.clear();
                    // Reload and display the updated list of banks
                    loadBankList();
                    // Show the "Delete" and "Update" sections
                   // showDeleteAndUpdateSections(true);
                } else {
                    showErrorMessage("Failed to Create Bank!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorMessage("An error occurred while creating the bank.");
            }
        } else {
            showErrorMessage("Bank name cannot be empty.");
        }
    }

    @FXML
    public void deleteSelectedBank() {
        String selectedBank = bankListView.getSelectionModel().getSelectedItem();
        if (selectedBank != null) {
            try (Connection connection = DBConn.connectDB()) {
                String sql = "DELETE FROM Bank WHERE bank_name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, selectedBank);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showSuccessMessage("Bank Deleted Successfully!");
                    // Reload and display the updated list of banks
                    loadBankList();
                } else {
                    showErrorMessage("Failed to Delete Bank!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorMessage("An error occurred while deleting the bank.");
            }
        } else {
            showErrorMessage("Please select a bank to delete.");
        }
    }
    // Helper method to toggle the visibility of "Delete" and "Update" sections
    private void showDeleteAndUpdateSections(boolean show) {
        deleteAndUpdateContainer.setVisible(show);
    }
    private void loadBankList() {
        bankNames.clear();

        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT bank_name FROM Bank";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bankNames.add(resultSet.getString("bank_name"));
            }

            bankListView.setItems(bankNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void updateSelectedBank() {
        String selectedBank = bankListView.getSelectionModel().getSelectedItem();
        String updatedBankName = updateBankNameField.getText();

        if (selectedBank != null && !updatedBankName.isEmpty()) {
            try (Connection connection = DBConn.connectDB()) {
                String sql = "UPDATE Bank SET bank_name = ? WHERE bank_name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, updatedBankName);
                preparedStatement.setString(2, selectedBank);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showSuccessMessage("Bank Updated Successfully!");
                    updateBankNameField.clear();
                    // Reload and display the updated list of banks
                    loadBankList();
                } else {
                    showErrorMessage("Failed to Update Bank!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorMessage("An error occurred while updating the bank.");
            }
        } else {
            showErrorMessage("Please select a bank and enter an updated bank name.");
        }
    }
    @FXML
    public void navigateBackToLandingPage() {
        try {
            // Load the LandingPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("landing-page.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) bankView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void showBankAccounts() {
        try {
            // Load the Bank.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bankAccount.fxml"));
            Parent root = loader.load();
            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) bankView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
