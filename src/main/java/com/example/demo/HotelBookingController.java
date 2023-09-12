        package com.example.demo;

        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.StackPane;
        import javafx.scene.layout.VBox;
        import javafx.stage.Modality;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;

        import java.net.StandardSocketOptions;
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

public class HotelBookingController {
    @FXML
    private TextField emailIdField;
    @FXML
    private ListView<Hotel> hotelListView;
    @FXML
    private ListView<HotelBookings> hotelBookingListView;

    @FXML
    private Button bookButton;
    private Hotel selectedHotel;
    private HotelBookings hotelBookings;

    @FXML
    private Button editButton;

    @FXML
    private DatePicker checkInDatePicker;
    @FXML
    private DatePicker checkOutDatePicker;

    private HotelBooking hotelBooking;

    @FXML
            private Button extendButton;
    @FXML
            private Button cancelButton;
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
        // Populate the ListView with hotels from the database
        try {
            retrieveHotelsFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Set up event handlers
        try {
            hotelListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                // Enable the book button when a hotel is selected
                bookButton.setDisable(newValue == null);
            });
        }
        catch(Exception e){

        }

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

        dialog.setResultConverter(buttonType -> {
            if (buttonType == bookButtonType) {
                // Perform the booking process
                LocalDate checkInDate = checkInDatePicker.getValue();
                LocalDate checkOutDate = checkOutDatePicker.getValue();

                if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
                    showAlert(AlertType.ERROR, "Invalid Dates", "Please select valid check-in and check-out dates.");
                    return null;
                }

                long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

                // Calculate the total cost based on the hotel's price and the number of days

                hotelBookings = hotelBookingListView.getSelectionModel().getSelectedItem();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                long days=ChronoUnit.DAYS.between(LocalDateTime.parse(hotelBookings.getCheck_in_time(),formatter),LocalDateTime.parse(hotelBookings.getCheck_out_time(),formatter));
                int hotelPrice=hotelBookings.getHotel_total_cost()/(int)days;
                int totalCost = hotelPrice * (int) daysBetween;
                HotelBooking hb=new HotelBooking();
                boolean bookingSuccess = hb.extendBooking(checkInDate.toString(),checkOutDate.toString(),hotelBookings.getHotel_booking_id(),totalCost);

                if (bookingSuccess) {
                    // Show a success message using an Alert
                    showAlert(AlertType.INFORMATION, "Booking Success", "Hotel booking changed successfully!");
                } else {
                    // Show an error message using an Alert
                    showAlert(AlertType.ERROR, "Booking Error", "Hotel booking failed.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void cancelButtonClicked() {
        hotelBookings = hotelBookingListView.getSelectionModel().getSelectedItem();
        HotelBooking hb=new HotelBooking();
        boolean bookingSuccess = hb.cancelBooking(hotelBookings.getHotel_booking_id());

        if (bookingSuccess) {
            // Show a success message using an Alert
            showAlert(AlertType.INFORMATION, "Booking Success", "Hotel booking changed successfully!");
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

        dialog.setResultConverter(buttonType -> {
            if (buttonType == bookButtonType) {
                // Perform the booking process
                LocalDate checkInDate = checkInDatePicker.getValue();
                LocalDate checkOutDate = checkOutDatePicker.getValue();

                if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
                    showAlert(AlertType.ERROR, "Invalid Dates", "Please select valid check-in and check-out dates.");
                    return null;
                }

                // Calculate the number of days between check-in and check-out
                long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

                // Calculate the total cost based on the hotel's price and the number of days

                hotelBookings = hotelBookingListView.getSelectionModel().getSelectedItem();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                long days=ChronoUnit.DAYS.between(LocalDateTime.parse(hotelBookings.getCheck_in_time(),formatter),LocalDateTime.parse(hotelBookings.getCheck_out_time(),formatter));
                int hotelPrice=hotelBookings.getHotel_total_cost()/(int)days;
                int totalCost = hotelPrice * (int) daysBetween;
                HotelBooking hb=new HotelBooking();
                boolean bookingSuccess = hb.editBooking(checkInDate.toString(), checkOutDate.toString(),hotelBookings.getHotel_booking_id(),totalCost);

                if (bookingSuccess) {
                    // Show a success message using an Alert
                    showAlert(AlertType.INFORMATION, "Booking Success", "Hotel booking changed successfully!");
                } else {
                    // Show an error message using an Alert
                    showAlert(AlertType.ERROR, "Booking Error", "Hotel booking failed.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void createBookingClicked() {
        //boolean isVisible = hotelListView.isVisible();
        //if(!isVisible)
        try{retrieveHotelsFromDatabase();}
        catch(Exception e){
            e.printStackTrace();
        }
        hotelBookingListView.setVisible(false);
        editButton.setVisible(false);
        extendButton.setVisible(false);
        cancelButton.setVisible(false);
        hotelListView.setVisible(true);
       // boolean isVisiblee = bookButton.isVisible();
        //if(!isVisiblee)
       bookButton.setVisible(true);
    }
    @FXML
    private void viewButtonClicked() {
       // boolean isVisible = hotelBookingListView.isVisible();
       // if(!isVisible)
        hotelListView.setVisible(false);
        bookButton.setVisible(false);
       hotelBookingListView.setVisible(true);
       editButton.setVisible(true);
       extendButton.setVisible(true);
       cancelButton.setVisible(true);
        String emailId=HotelBooking.getInstance().getEmailId();
        HotelBooking c= new HotelBooking();
        int userId=c.getUserdetails(emailId);
        String sql = "SELECT * FROM hotel_booking where user_id=?";

        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
                        ObservableList<HotelBookings> hotelBookings = FXCollections.observableArrayList();

                        while (resultSet.next()) {
                            int hotelBookingId = resultSet.getInt("hotel_booking_id");
                            int accountId = resultSet.getInt("account_id");
                            String checkInTime = resultSet.getString("check_in_time");
                            String checkOutTime = resultSet.getString("check_out_time");
                            int hotelTotalCost = resultSet.getInt("hotel_total_cost");

                            HotelBookings hotelBooking = new HotelBookings(hotelBookingId, accountId, checkInTime, checkOutTime, hotelTotalCost);
                            hotelBookings.add(hotelBooking);
                        }

                        hotelBookingListView.setItems(hotelBookings);
                    }

         catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleHotelSelection(MouseEvent event) {
        this.selectedHotel = hotelListView.getSelectionModel().getSelectedItem();

            }

    @FXML
    private void handleHotelBookingSelection(MouseEvent event) {
       this.hotelBookings = hotelBookingListView.getSelectionModel().getSelectedItem();

    }

    @FXML
    private void onBookButtonClicked() {
        this.selectedHotel = hotelListView.getSelectionModel().getSelectedItem();
        if (selectedHotel != null) {
            showBookingDialog(selectedHotel);
        }
    }

    private void retrieveHotelsFromDatabase() throws SQLException {
        // Fetch hotel data from the database and populate the ListView
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

                hotelListView.setItems(hotels);

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
                LocalDate checkInDate = checkInDatePicker.getValue();
                LocalDate checkOutDate = checkOutDatePicker.getValue();

                if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
                    showAlert(AlertType.ERROR, "Invalid Dates", "Please select valid check-in and check-out dates.");
                    return null;
                }

                // Calculate the number of days between check-in and check-out
                long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

                // Calculate the total cost based on the hotel's price and the number of days
                int totalCost = selectedHotel.getHotelPrice() * (int) daysBetween;

                // Perform the booking process
                String emailId = HotelBooking.getInstance().getEmailId();
                HotelBooking c = new HotelBooking();
                int userId = c.getUserdetails(emailId);
                HotelBooking hotelbookingg=new HotelBooking();
                boolean bookingSuccess = hotelbookingg.createBooking(selectedHotel.getHotelId(), userId,1234 ,
                        checkInDate.toString(),
                        checkOutDate.toString(),totalCost);

                if (bookingSuccess) {
                    // Show a success message using an Alert
                    showAlert(AlertType.INFORMATION, "Booking Success", "Hotel booked successfully! Total Cost: $" + totalCost);
                } else {
                    // Show an error message using an Alert
                    showAlert(AlertType.ERROR, "Booking Error", "Hotel booking failed.");
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

        // Customize the layout of the dialog content
        VBox content = new VBox(10); // Vertical layout with spacing
        content.getChildren().addAll(
                new Label("Check-In Date:"),
                checkInDatePicker,
                new Label("Check-Out Date:"),
                checkOutDatePicker
        );

        dialogPane.setContent(content);

        return dialogPane;
    }

}
