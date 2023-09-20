package com.example.demo;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Parent;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


import javafx.stage.Window;
import javafx.util.StringConverter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LandingPageController extends NightLifeController implements Initializable {
    @FXML
    private Button profileLink,newCard,nextButton,previousButton, adminButton;
    @FXML
    private StackPane userDataStackPane;
    private boolean isProfilePaneOpen = false;
    @FXML
    private Pane weatherPane;
    @FXML
    private ImageView newsImage1, newsImage2, newsImage3;
    @FXML
    private DialogPane descriptionPane1, descriptionPane2, descriptionPane3;
    @FXML
    private Hyperlink newsLink1, newsLink2, newsLink3;
    @FXML
    private Label busLabel;
    @FXML
    private TableView<Job> jobTableView;
    @FXML
    private TableColumn<Job, String> titleColumn, gradeColumn, agencyColumn, locationColumn, applyColumn;

    @FXML
    private VBox transport;

    @FXML
    private HBox editJobRow;

    private int currentNewsIndex = 0; // Initialize to 0
    private int newsPerPage = 3;// Number of news articles to display at a time


    private int flag = 0;

    private Job currentJob = new Job(0);

    @FXML
    private TableView<Bus> transportTable;
    @FXML
    TableColumn<Bus, String> busNo, busName, routeDescription;
    @FXML
    Spinner<Integer> ticketSpinner;

    @FXML
    private TableView<Stop> stopsTable;
    @FXML
    private TableColumn<Stop, String> busStop, busArrivalTime, busDepartureTime;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
            ticketSpinner.setValueFactory(valueFactory);

            ObservableList<News> newsItems = FXCollections.observableArrayList(News.getNews());

            // Initialize the dialog panes and images for the first news article
            populateNewsPanes(newsItems, currentNewsIndex);

            // Add an event handler for the "Next" button
            nextButton.setOnAction(event -> {
                if (currentNewsIndex + 1 < newsItems.size() - 2) {
                    currentNewsIndex++;
                    populateNewsPanes(newsItems, currentNewsIndex);
                }
            });
            previousButton.setOnAction(event -> {
                if (currentNewsIndex - 1 < newsItems.size() - 2) {
                    if (currentNewsIndex == 0) {
                        currentNewsIndex = 1;
                    }
                    currentNewsIndex--;
                    populateNewsPanes(newsItems, currentNewsIndex);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        profileLink.setOnAction(this::handleProfileButtonClick);
        show();
        displayWeather();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    // Update UI elements on the JavaFX Application Thread
                    busNo.setCellValueFactory(cellData -> cellData.getValue().getShortName());
                    busName.setCellValueFactory(cellData -> cellData.getValue().getLongName());
                    routeDescription.setCellValueFactory(cellData -> cellData.getValue().getRouteID());

                    // Transport Table
                    try {
                        transportTable.getItems().addAll(TransportController.getBuses());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                return null;
            }
        };

        // Execute the task in the separate thread
        executorService.submit(task);

        busStop.setCellValueFactory(cellData -> cellData.getValue().stopNameProperty());
        busArrivalTime.setCellValueFactory(cellData -> cellData.getValue().arrivalTimeProperty());
        busDepartureTime.setCellValueFactory(cellData -> cellData.getValue().departureTimeProperty());

        transportTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        transportTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Detect a single click
                Bus selectedBus = transportTable.getSelectionModel().getSelectedItem();
                busLabel.setText("Bus #" + selectedBus.getShortName().getValue());
                if (selectedBus != null) {
                    // Clear the existing items in stopsTable
                    stopsTable.getItems().clear();
                    // Get the stops from the selected Bus object and populate the stopsTable
                    try {
                        ArrayList<Stop> stopList = TransportController.getStops(selectedBus.getRouteMainId());
                        LocalTime currentTime = LocalTime.now();
                        int currentHour = currentTime.getHour();

                        for(int i = 0; i < stopList.size(); i++){
                            SimpleStringProperty arrivalTimeProperty = stopList.get(i).arrivalTime;
                            String arrivalTime = arrivalTimeProperty.get();
                            String newTime = currentHour + arrivalTime.substring(1);
                            arrivalTimeProperty.set(newTime);

                            SimpleStringProperty departureTimeProperty = stopList.get(i).departureTime;
                            String departureTime = departureTimeProperty.get();
                            String newDTime = currentHour + departureTime.substring(1);
                            departureTimeProperty.set(newDTime);
                        }
                        stopsTable.getItems().addAll(stopList);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        titleColumn.setMinWidth(348);
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().JobGradeProperty());

        agencyColumn.setCellValueFactory(cellData -> cellData.getValue().JobAgencyProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().JobLocationProperty());
        applyColumn.setCellFactory(column -> new TableCell<Job, String>() {

            final Button applyButton = new Button("Apply");

            {
                // Set the action for the apply button
                applyButton.setOnAction(event -> {
                    // Here, you can handle the logic for applying to the job
                    // You can access the Job object associated with this row using getItem()
                    Job job = getTableView().getItems().get(getIndex());
                    if (job != null) {
                        // Display a confirmation dialog
                        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationAlert.setTitle("Confirmation");
                        confirmationAlert.setHeaderText("Apply for the Job");
                        confirmationAlert.setContentText("Are you sure you want to apply for this job?");

                        Optional<ButtonType> result = confirmationAlert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            // User clicked OK, so you can call the apply method here
                            //job.apply(); // Call the apply method on your Job object
                            JobListing.applyJob(job.getJobId());

                        }
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // No button in empty cells
                    setText(null);
                } else {
                    setGraphic(applyButton);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // Display only the button
                    setAlignment(Pos.CENTER); // Center-align the button
                }
            }
        });
        // Populate the TableView with job listings from JobListing class
        jobTableView.getItems().addAll(JobListing.getAllJobs());
        System.out.println("This is where admin check would go");
        if(User.getInstance().getRoleID() == 2){

        }
        //todo limit to admin only 
        setJobEditRowBehavior();
    }

    private void setJobEditRowBehavior() {
        //what does this do?
        //spawn 3 button on load
        //change it to text box with 2 buttons
        //invoke the various commands in Job Controller or something
        Button b1 = new Button("Edit");
        Button b2 = new Button("Add");
        Button b3 = new Button("Delete");
        editJobRow.getChildren().addAll(b1, b2, b3);
        Button[] buttons = {b1, b2, b3};

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                flag = 1;
                System.out.println("hello");

                editJobRow.getChildren().removeAll(b1, b2, b3);

                jobEditTransition();
            }
        });

        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                flag = 2;
                System.out.println("hello");

                editJobRow.getChildren().removeAll(b1, b2, b3);

                jobEditTransition();
            }
        });

        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                flag = 3;
                System.out.println("hello");
                editJobRow.getChildren().removeAll(b1, b2, b3);

                jobEditTransition();

            }
        });
    }

    private void jobEditTransition() {
        TextField title = new TextField();
        TextField grade = new TextField();
        TextField agency = new TextField();
        TextField city = new TextField();
        Button ok = new Button("Confirm");
        Button cancel = new Button("Cancel");

        title.setPromptText("Job Title");
        title.setFocusTraversable(false);

        grade.setPromptText("Grade");
        grade.setFocusTraversable(false);

        agency.setPromptText("Agency");
        agency.setFocusTraversable(false);

        city.setPromptText("City");
        city.setFocusTraversable(false);

        if(flag == 3){
            title.setDisable(true);
            grade.setDisable(true);
            agency.setDisable(true);
            city.setDisable(true);
        }

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                editJobRow.getChildren().removeAll(title, grade, agency, city, ok, cancel);
                setJobEditRowBehavior();
            }
        });

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                modJobListing(title.getText(), grade.getText(), agency.getText(), city.getText());
                editJobRow.getChildren().removeAll(title, grade, agency, city, ok, cancel);
                jobEditTransition();

            }
        });

        //defines behavior when
        jobTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                currentJob = jobTableView.getSelectionModel().getSelectedItem();
                title.setText(currentJob.getJobTitle());
                city.setText(currentJob.getJobLocation());
                grade.setText(currentJob.getJobGrade());
                agency.setText(currentJob.getJobAgency());
            }
        });


        editJobRow.getChildren().addAll(title, grade, agency, city, ok, cancel);

        //get random job id
        //title grade agency city
    }


    private void modJobListing(String title, String grade, String agency, String city) {
        //1 edit 2 add 3 delete
        try {
            if (flag == 1) {
                jobTableView.getItems().remove(currentJob);
                Job newJob = new Job(title,currentJob.getJobId(),grade,agency,city);
                JobListing.editJob(currentJob,newJob);
                jobTableView.getItems().add(0,newJob);
            } else if (flag == 2) {
                currentJob = JobListing.addJob(title, grade, agency, city);
                jobTableView.getItems().add(0,currentJob);
            } else if (flag == 3) {
                JobListing.removeJob(currentJob);
                jobTableView.getItems().remove(currentJob);
            }
        } catch (SQLException e) {
            System.out.println("sql error");
        }
    }

    private void populateNewsPanes(ObservableList<News> newsItems, int startIndex) {
        for (int i = 0; i < newsPerPage; i++) {
            int newsIndex = startIndex + i;
            if (newsIndex < newsItems.size()) {
                News news = newsItems.get(newsIndex);
                ImageView imageView = getImagePaneForIndex(i);
                DialogPane descriptionPane = getDescriptionPaneForIndex(i);

                try {
                    if (news.getImg_url() == "null") {
                        Image image = new Image("file:src/main/resources/com/example/images/MicrosoftTeams-image.png");
                        imageView.setImage(image);
                    } else {
                        Image image = new Image(news.getImg_url());
                        imageView.setImage(image);
                    }

                    // Center the image within the ImageView
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(255); // Adjust the width as needed
                    imageView.setFitHeight(150); // Adjust the height as needed


                } catch (IllegalArgumentException e) {
                    // Handle the case where the image URL is invalid or not found
                    // Set a placeholder image or display an error message
                    imageView.setImage(null); // Set to a placeholder or null
                }
                Hyperlink newsLink = getNewsLinkForIndex(i);
                newsLink.setText("Go to Source");
                newsLink.setUnderline(true);
                newsLink.setOnAction(event -> {
                    // Open the URL in a web browser when the hyperlink is clicked
                    String url = news.getUrl();
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
                descriptionPane.setContentText(news.getDescription());
                descriptionPane.setHeaderText(news.getTitle());
            } else {
                // If there are no more news articles, clear the remaining panes
                ImageView imageView = getImagePaneForIndex(i);
                DialogPane descriptionPane = getDescriptionPaneForIndex(i);
                imageView.setImage(null);
                descriptionPane.setContentText("");
                descriptionPane.setHeaderText("");

            }
        }
    }

    private void handleProfileButtonClick(ActionEvent event) {
        if (!isProfilePaneOpen) {
            // Create and populate the user data pane
            Pane userDataPane = createUserProfilePane(); // Implement this method
            userDataStackPane.setVisible(true);
            // Add the user data pane to the StackPane
            userDataStackPane.getChildren().add(userDataPane);

            // Set the flag to indicate that the profile pane is open
            isProfilePaneOpen = true;
        } else {
            // Close the user data pane
            userDataStackPane.getChildren().clear();
            userDataStackPane.setVisible(false);

            // Set the flag to indicate that the profile pane is closed
            isProfilePaneOpen = false;
        }
    }

    private void displayWeather() {
        Weather weather = Weather.getWeather();

        if (weather != null) {
//            //for maaz to fix later


            // Create a URL object for the image
            URL imageUrl = null;
            try {
                imageUrl = new URL("https:" + weather.getConditionIcon());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ImageView iconImageView = new ImageView();
            // Open a stream to read the image data
            InputStream stream = null;
            try {
                stream = imageUrl.openStream();
                // Create an Image object from the input stream
                Image image = new Image(stream);

                iconImageView.setLayoutX(10.0);
                iconImageView.setLayoutY(0.0);
                Image conditionIconImage = image;
                iconImageView.setImage(conditionIconImage);
                // Close the input stream
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Label temperatureLabel = new Label(weather.getTempFarhenhiet() + "°F");
            temperatureLabel.setLayoutX(100.0);
            temperatureLabel.setLayoutY(10.0);
            temperatureLabel.setFont(new Font(30));


            Label conditionLabel = new Label(weather.getConditionText());

// Calculate the X position to center the condition label with respect to temperatureLabel
            double temperatureLabelWidth = new Text(temperatureLabel.getText()).getLayoutBounds().getWidth();
            double conditionLabelWidth = new Text(conditionLabel.getText()).getLayoutBounds().getWidth();
            double conditionLabelX = temperatureLabel.getLayoutX() + (temperatureLabelWidth - conditionLabelWidth) / 2;

// Set the calculated X position
            conditionLabel.setLayoutX(conditionLabelX);

// Align vertically with temperatureLabel
            double conditionLabelY = 45.0; // Align vertically with temperatureLabel
            conditionLabel.setLayoutY(conditionLabelY);

            Label humidityLabel = new Label("Humidity: " + weather.getHumidity());
            humidityLabel.setLayoutX(10.0);
            humidityLabel.setLayoutY(60.0);

            Label percipLabel = new Label("Precipitation: " + weather.getPrecipIn());
            percipLabel.setLayoutX(10.0);
            percipLabel.setLayoutY(80.0);

            Label windLabel = new Label("Wind: " + weather.getWindMph());
            windLabel.setLayoutX(190.0);
            windLabel.setLayoutY(60.0);
            windLabel.setTextAlignment(TextAlignment.RIGHT);

            Label uvLabel = new Label("UV Index: " + weather.getUvIndex());
            uvLabel.setLayoutX(190.0);
            uvLabel.setLayoutY(80.0);
            uvLabel.setTextAlignment(TextAlignment.RIGHT);


            // Add these labels to the weatherPane
            weatherPane.getChildren().addAll(temperatureLabel, conditionLabel, humidityLabel, percipLabel, windLabel, uvLabel, iconImageView);
        } else {
            System.out.println("Failed to fetch weather data.");
        }
    }

    // Implement this method to create and populate the user data pane
    private Pane createUserProfilePane() {
        // Create a Pane for user data
        Pane userDataPane = new Pane();

        // Populate the user data Pane with user-specific content
        Text userDataText = new Text("Name: " + User.getInstance().getFirstName() + " " + User.getInstance().getLastName()
                + "\nEmail: " + User.getInstance().getEmail()
                + "\nPhone number: " + User.getInstance().getPhoneNumber()
                + "\nUser role ID: " + User.getInstance().getRoleID() // Replace with your user data components
                + "\nAddress: \n" + User.getInstance().getStreetAddress()
                + "\n" + User.getInstance().getCity()
                + ", " + User.getInstance().getZipcode()
                + ", " + User.getInstance().getState());
        userDataText.setLayoutX(10);
        userDataText.setLayoutY(10);
        adminButton = new Button("Add new Admin");
        adminButton.setLayoutX(10);
        adminButton.setLayoutY(200);

// Add an event handler to the button
        adminButton.setOnAction(event -> loadAdminFXML());
        newCard = new Button("Add a New Payment Method"); // Replace with your user data components
        newCard.setLayoutX(10);
        newCard.setLayoutY(250);

        // Add an event handler to the button
        newCard.setOnAction(event -> loadBankFXML());

        userDataPane.getChildren().add(userDataText);
        userDataPane.getChildren().add(newCard);
        userDataPane.getChildren().add(adminButton);
        return userDataPane;
    }
    private void onProfileLinkClicked() {
        loadBankFXML();
    }
    private void loadBankFXML() {
        try {
            // Load the Bank.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bankAccount.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) newCard.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAdminFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminpanel.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) adminButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * promoteToAdmin()
     * Fails if:
     *  1) A username wasn't entered
     *  2) The username matches the current User
     *  3) An account with the entered username wasn't found
     *  4) The account is already an admin
     * Promotes a User to an Admin if successful
     */
    private void promoteToAdmin() {

        Window owner = adminButton.getScene().getWindow();

        TextInputDialog adminPane = new TextInputDialog("User name");
        adminPane.setHeaderText("Enter the user name of the account you wish to promote.");

        // Get username entered in by the admin
        String userName = adminPane.getEditor().getText();

        // Stop if user didn't enter a username
        if ( userName == null ) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Username wasn't entered");
            return;
        }

        // Prevent user from promoting themselves
        if ( userName == User.getInstance().getEmail()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "You can't promote yourself");
            return;
        }

        // Search for an account with that username
        final String SELECT_QUERY = "SELECT * FROM user WHERE user_email = ?";
        // try connecting to the database
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, userName);
            // execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // An account wasn't found
            if (resultSet.next() != true) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "An account with that username doesn't exist");
                return;
            }

            // The account is already an admin
            if (Integer.parseInt(resultSet.getString(11)) == 2) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "That account is already an Admin");
                return;
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }

        // Everything is ok, start updating the database
        final String UPDATE_QUERY = "UPDATE user SET role_ID = 2 WHERE user_email = ?";
        // try connecting to the database
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, userName);
            // execute query
            preparedStatement.executeQuery();

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    public void LogOut(ActionEvent event) throws SQLException, IOException {

        // Log user out of account
        User.logOut();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("logged-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 544, 400);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("Smart City - Sign up");
        stage.setScene(scene);
        stage.show();
    }

    public ImageView getImagePaneForIndex(int index) {
        switch (index) {
            case 0:
                return newsImage1;
            case 1:
                return newsImage2;
            case 2:
                return newsImage3;
            default:
                // Handle any other index as needed
                return null;
        }
    }

    public DialogPane getDescriptionPaneForIndex(int index) {
        switch (index) {
            case 0:
                return descriptionPane1;
            case 1:
                return descriptionPane2;
            case 2:
                return descriptionPane3;
            default:
                // Handle any other index as needed
                return null;
        }
    }

    public Hyperlink getNewsLinkForIndex(int index) {
        switch (index) {
            case 0:
                return newsLink1;
            case 1:
                return newsLink2;
            case 2:
                return newsLink3;
            // Add more cases if you have more Hyperlink fields in your layout
            default:
                throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    @FXML
    public void purchaseTransportTicket(){
        TransportController.purchaseTicket(ticketSpinner.getValue());
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
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
