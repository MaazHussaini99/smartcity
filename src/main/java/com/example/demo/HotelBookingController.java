package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class HotelBookingController {
    @FXML
    private TextField emailIdField;
    @FXML
    private TableView<Hotel> hotelTableView;
    @FXML
    private TableView<HotelBookings> hotelBookingListView;
    @FXML
    private TableColumn<Hotel, String> hotelNameColumn;
    @FXML
    private TableColumn<Hotel, String> hotelLocationColumn;
    @FXML
    private TableColumn<Hotel, Integer> hotelPriceColumn;
    @FXML
    private TableColumn<Hotel, Integer> hotelRoomNumberColumn;
    @FXML
    private TableColumn<Hotel, Integer> hotelAvailabilityColumn;
    @FXML
    private TableColumn<HotelBookings, Integer> hotelBookingIdColumn;
    @FXML
    private TableColumn<HotelBookings, String> hotelNameeColumn;
    @FXML
    private TableColumn<HotelBookings, String> hotelLocationnColumn;
    @FXML
    private TableColumn<HotelBookings, String> hotelCheckInTimeColumn;
    @FXML
    private TableColumn<HotelBookings, String> hotelCheckOutTimeColumn;
    @FXML
    private TableColumn<HotelBookings, Integer> hotelTotalCostColumn;
    @FXML
    private Button bookButton;
    private Hotel selectedHotel;
    private HotelBookings hotelBookings;
    @FXML
    private Button editButton;
    @FXML
    private HBox manageHotels;
    @FXML
    private DatePicker checkInDatePicker;
    @FXML
    private DatePicker checkOutDatePicker;
    private HotelBooking hotelBooking;
    @FXML
    private Button extendButton;
    @FXML
    private Button bookHotels;
    @FXML
    private Button cancelButton;
    @FXML
    private Button manageHotelsBookings;
    @FXML
    private TextField numberofRooms;
    String emailId;
    int userId;
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    private int accountId;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    public HotelBookingController(String emailId) {
        this.emailId = emailId;
    }
    public HotelBookingController() {
    }
    @FXML
    private ImageView hotelImageView;
    public void initialize() {
        // Initialize the hotelBooking object
        hotelBooking = new HotelBooking();
        // Set up TableView columns and data binding for hotels
        hotelNameColumn.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        hotelLocationColumn.setCellValueFactory(new PropertyValueFactory<>("hotelLocation"));
        hotelPriceColumn.setCellValueFactory(new PropertyValueFactory<>("hotelPrice"));
        hotelRoomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("hotelRoomNumber"));
        hotelAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("hotelAvailability"));
        hotelBookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("hotelBookingId"));
        hotelNameeColumn.setCellValueFactory(new PropertyValueFactory<>("hotelNamee"));
        hotelLocationnColumn.setCellValueFactory(new PropertyValueFactory<>("hotelLocationn"));
        hotelCheckInTimeColumn.setCellValueFactory(new PropertyValueFactory<>("hotelCheckInTime"));
        hotelCheckOutTimeColumn.setCellValueFactory(new PropertyValueFactory<>("hotelCheckOutTime"));
        hotelTotalCostColumn.setCellValueFactory(new PropertyValueFactory<>("hotelTotalCost"));
        // Populate the TableView with hotels from the database
        try {
            retrieveHotelsFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Set up event handlers for hotel selection
        hotelTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Enable the book button when a hotel is selected
            bookButton.setDisable(newValue == null);
        });
    }
    @FXML
    private void extendButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Extend Booking");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(createBookingDialogContent());
        ButtonType bookButtonType = new ButtonType("Extend");
        dialogPane.getButtonTypes().addAll(bookButtonType, ButtonType.CANCEL);
        LocalDate today = LocalDate.now();
        dialog.setResultConverter(buttonType -> {
            if (buttonType == bookButtonType) {
                // Perform the booking process
               int rooms = Integer.parseInt(numberofRooms.getText());
                LocalDate checkInDate = checkInDatePicker.getValue();
                LocalDate checkOutDate = checkOutDatePicker.getValue();
                if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
                    showAlert(AlertType.ERROR, "Invalid Dates", "Please select valid check-in and check-out dates.");
                    return null;
                }
                else if (checkInDate.isBefore(today)) {
                    showAlert(AlertType.ERROR, "Invalid Check-In Date", "Check-in date should be from today or later.");
                    return null;
                }
                long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
                if(daysBetween == 0) {
                    daysBetween = daysBetween + 1;
                }
                // Calculate the total cost based on the hotel's price and the number of days
                hotelBookings = hotelBookingListView.getSelectionModel().getSelectedItem();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                long days = ChronoUnit.DAYS.between(LocalDateTime.parse(hotelBookings.getCheckInTime(), formatter),
                        LocalDateTime.parse(hotelBookings.getCheckOutTime(), formatter));
                if (days == 0) {
                    days = days + 1;
                }
                HotelBooking b=new HotelBooking();
                int hotelId=b.getHotelId(hotelBookings.getHotelBookingId());
                int hotelPrice =b.getHotelPrice(hotelId);
                int totalCost = hotelPrice * (int) daysBetween *rooms;
                HotelBooking hb = new HotelBooking();
                int previousHotelBookingCost = hb.getHotelBookingCost(hotelBookings.getHotelBookingId());
                boolean bookingSuccess = false;
                try {
                    bookingSuccess = hb.extendBooking(checkInDate.toString(), checkOutDate.toString(),
                            hotelBookings.getHotelBookingId(), totalCost);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String emailId = HotelBooking.getInstance().getEmailId();
                int userId = hb.getUserdetails(emailId);
                int accountId = hb.getAccountId(userId);
                int amountt = (int) (hb.getAccountBalance(accountId));
                int balance = amountt - totalCost + previousHotelBookingCost;
                if (bookingSuccess) {
                    hb.updateAccountBalance(accountId, balance);
                    showAlert(AlertType.INFORMATION, "Booking Success",
                            "Hotel booking changed successfully! Updated Cost: $" + totalCost);
                } else {
                    // Show an error message using an Alert
                    showAlert(AlertType.ERROR, "Booking Error", "Hotel booking failed.  Please check balance");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }
    @FXML
    private void cancelButtonClicked() {
        hotelBookings = hotelBookingListView.getSelectionModel().getSelectedItem();
        HotelBooking hb = new HotelBooking();
        int previousHotelBookingCost = hb.getHotelBookingCost(hotelBookings.getHotelBookingId());
        boolean bookingSuccess = hb.cancelBooking(hotelBookings.getHotelBookingId());
        String emailId = HotelBooking.getInstance().getEmailId();
        int userId = hb.getUserdetails(emailId);
        int accountId = hb.getAccountId(userId);
        int amountt = (int) (hb.getAccountBalance(accountId));
        int balance = amountt + previousHotelBookingCost;
        if (bookingSuccess) {
            hb.updateAccountBalance(accountId, balance);
            // Show a success message using an Alert
            showAlert(AlertType.INFORMATION, "Booking Success", "Hotel booking cancelled successfully! Amount Refunded: $"
                    + previousHotelBookingCost);
        } else {
            // Show an error message using an Alert
            showAlert(AlertType.ERROR, "Booking Error", "Hotel booking failed.");
        }
    }
    @FXML
    private void oneditButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Booking");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(createBookingDialogContent());
        ButtonType bookButtonType = new ButtonType("Edit");
        dialogPane.getButtonTypes().addAll(bookButtonType, ButtonType.CANCEL);
        LocalDate today = LocalDate.now();
        dialog.setResultConverter(buttonType -> {
            if (buttonType == bookButtonType) {
                // Perform the booking process
                int rooms = Integer.parseInt(numberofRooms.getText());
                LocalDate checkInDate = checkInDatePicker.getValue();
                LocalDate checkOutDate = checkOutDatePicker.getValue();
                if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
                    showAlert(AlertType.ERROR, "Invalid Dates", "Please select valid check-in and check-out dates.");
                    return null;
                }
                else if (checkInDate.isBefore(today)) {
                    showAlert(AlertType.ERROR, "Invalid Check-In Date", "Check-in date should be from today or later.");
                    return null;
                }
                // Calculate the number of days between check-in and check-out
                long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
                if(daysBetween == 0) {
                    daysBetween = daysBetween + 1;
                }
                // Calculate the total cost based on the hotel's price and the number of days
                hotelBookings = hotelBookingListView.getSelectionModel().getSelectedItem();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                long days = ChronoUnit.DAYS.between(LocalDateTime.parse(hotelBookings.getCheckInTime(), formatter),
                        LocalDateTime.parse(hotelBookings.getCheckOutTime(), formatter));
                if (days == 0) {
                    days = days + 1;
                }
                HotelBooking b=new HotelBooking();
                int hotelId=b.getHotelId(hotelBookings.getHotelBookingId());
                int hotelPrice =b.getHotelPrice(hotelId);
                int totalCost = hotelPrice * (int) daysBetween * rooms;
                HotelBooking hb = new HotelBooking();
                int previousHotelBookingCost = hb.getHotelBookingCost(hotelBookings.getHotelBookingId());
                boolean bookingSuccess = false;
                try {
                    bookingSuccess = hb.extendBooking(checkInDate.toString(), checkOutDate.toString(),
                            hotelBookings.getHotelBookingId(), totalCost);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String emailId = HotelBooking.getInstance().getEmailId();
                int userId = hb.getUserdetails(emailId);
                int accountId = hb.getAccountId(userId);
                int amountt = (int) (hb.getAccountBalance(accountId));
                int balance = amountt - totalCost + previousHotelBookingCost;
                if (bookingSuccess) {
                    hb.updateAccountBalance(accountId, balance);
                    showAlert(AlertType.INFORMATION, "Booking Success",
                            "Hotel booking changed successfully! Updated Cost: $" + totalCost);
                } else {
                    // Show an error message using an Alert
                    showAlert(AlertType.ERROR, "Booking Error", "Hotel booking failed.  Please check balance");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }
    @FXML
    private void createBookingClicked() {
        try {
            retrieveHotelsFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        manageHotels.setVisible(false);
        hotelBookingListView.setVisible(false);
        editButton.setVisible(false);
        bookHotels.setVisible(false);
        extendButton.setVisible(false);
        cancelButton.setVisible(false);
        hotelTableView.setVisible(true);
        bookButton.setVisible(true);
        manageHotelsBookings.setVisible(true);
    }
    @FXML
    private void viewButtonClicked() {
        manageHotels.setVisible(false);
        manageHotelsBookings.setVisible(false);
        hotelTableView.setVisible(false);
        bookButton.setVisible(false);
        hotelBookingListView.setVisible(true);
        editButton.setVisible(true);
        extendButton.setVisible(true);
        cancelButton.setVisible(true);
        bookHotels.setVisible(true);
        String emailId = HotelBooking.getInstance().getEmailId();
        HotelBooking c = new HotelBooking();
        int userId = c.getUserdetails(emailId);
        String sql = "SELECT * FROM hotel_booking where user_id=?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<HotelBookings> hotelBookings = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int hotelBookingId = resultSet.getInt("hotel_booking_id");
                int hotelId=resultSet.getInt("hotel_id");
                int accountId = resultSet.getInt("account_id");
                String checkInTime = resultSet.getString("check_in_time");
                String checkOutTime = resultSet.getString("check_out_time");
                int hotelTotalCost = resultSet.getInt("hotel_total_cost");
                HotelBooking h=new HotelBooking();
                String hotelName=h.getHotelBookingName(hotelId);
                String hotelLocation=h.getHotelBookingLocation(hotelId);
                HotelBookings hotelBooking = new HotelBookings(hotelBookingId,hotelName,hotelLocation,checkInTime,checkOutTime,hotelTotalCost);
                hotelBookings.add(hotelBooking);
            }
            hotelBookingListView.setItems(hotelBookings);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void handleHotelSelection(MouseEvent event) {
        selectedHotel = hotelTableView.getSelectionModel().getSelectedItem();
    }
    @FXML
    private void handleHotelBookingSelection(MouseEvent event) {
        hotelBookings = hotelBookingListView.getSelectionModel().getSelectedItem();
    }
    @FXML
    private void onBookButtonClicked() {
        Hotel selectedHotel = hotelTableView.getSelectionModel().getSelectedItem();
        if (selectedHotel != null) {
            showBookingDialog(selectedHotel);
        }
    }
    private void retrieveHotelsFromDatabase() throws SQLException {
        // Fetch hotel data from the database and populate the TableView
        String sql = "SELECT * FROM hotel";
        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                ObservableList<Hotel> hotels = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    int hotelId = resultSet.getInt("hotel_id");
                    String hotelName = resultSet.getString("hotel_name");
                    String location = resultSet.getString("hotel_location");
                    int price = resultSet.getInt("hotel_price");
                    int roomNo = resultSet.getInt("hotel_room_no");
                    int availability = resultSet.getInt("hotel_availibility");
                    Hotel hotel = new Hotel(hotelId, hotelName, location, price, roomNo, availability);
                    hotels.add(hotel);
                }
                hotelTableView.setItems(hotels);
            }
        }
    }
    // ...
    private void showBookingDialog(Hotel selectedHotel) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Book Hotel");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(createBookingDialogContent());
        ButtonType bookButtonType = new ButtonType("Book");
        dialogPane.getButtonTypes().addAll(bookButtonType, ButtonType.CANCEL);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == bookButtonType) {
                // Get the selected check-in and check-out dates from the DatePicker controls
                int rooms = Integer.parseInt(numberofRooms.getText());
                LocalDate checkInDate = checkInDatePicker.getValue();
                LocalDate checkOutDate = checkOutDatePicker.getValue();
                LocalDate today = LocalDate.now();
                if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
                    showAlert(AlertType.ERROR, "Invalid Dates", "Please select valid check-in and check-out dates.");
                    return null;
                }
                else if (checkInDate.isBefore(today)) {
                    showAlert(AlertType.ERROR, "Invalid Check-In Date", "Check-in date should be from today or later.");
                    return null;
                }
                // Calculate the number of days between check-in and check-out
                long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
                if (daysBetween == 0) {
                    daysBetween = daysBetween + 1;
                }
                // Calculate the total cost based on the hotel's price and the number of days
                int totalCost = selectedHotel.getHotelPrice() * (int) daysBetween*rooms;
                // Perform the booking process
                String emailId = HotelBooking.getInstance().getEmailId();
                HotelBooking c = new HotelBooking();
                int userId = c.getUserdetails(emailId);
                HotelBooking hb = new HotelBooking();
                int accountId = hb.getAccountId(userId);
                if (accountId == -1) {
                    showAlert(AlertType.ERROR, "Add Payment Method", "Please add a valid payment method");
                    return null;
                }
                this.accountId = accountId;
                boolean bookingSuccess = hb.createBooking(selectedHotel.getHotelId(), userId, accountId,
                        checkInDate.toString(), checkOutDate.toString(), totalCost);
                int amountt = (int) (hb.getAccountBalance(accountId));
                int updatedAmount = amountt - totalCost;
                if (bookingSuccess && (hb.updateAccountBalance(accountId, updatedAmount))) {
                    // Show a success message using an Alert
                    showAlert(AlertType.INFORMATION, "Booking Success",
                            "Hotel booked successfully! Total Cost: $" + totalCost);
                } else {
                    // Show an error message using an Alert
                    showAlert(AlertType.ERROR, "Booking Error", "Hotel booking failed. Please check balance");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }
    // Helper method to display an Alert
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private DialogPane createBookingDialogContent() {
        DialogPane dialogPane = new DialogPane();
        this.checkInDatePicker = new DatePicker();
        this.checkOutDatePicker = new DatePicker();
        this.numberofRooms=new TextField();
        // Customize the layout of the dialog content
        VBox content = new VBox(10); // Vertical layout with spacing
        content.getChildren().addAll(new Label("Enter number of rooms:"),numberofRooms,new Label("Check-In Date:"), checkInDatePicker, new Label("Check-Out Date:"),
                checkOutDatePicker);
        dialogPane.setContent(content);
        return dialogPane;
    }
}
