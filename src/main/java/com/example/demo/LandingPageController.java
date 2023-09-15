package com.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.util.StringConverter;

public class LandingPageController extends HotelBookingController implements Initializable {


    @FXML
    private Button profileLink,newCard,nextButton,previousButton;
    @FXML
    private StackPane userDataStackPane;
    private boolean isProfilePaneOpen = false;
    private ListView<String> newsList;

    @FXML
    private Pane weatherPane;
    @FXML
    private ImageView newsImage1,newsImage2,newsImage3;
    @FXML
    private DialogPane descriptionPane1,descriptionPane2,descriptionPane3;
    @FXML
    private Hyperlink newsLink1,newsLink2,newsLink3;
    @FXML
    private TableView<Job> jobTableView;
    @FXML
    private TableColumn<Job, String> titleColumn,gradeColumn,agencyColumn,locationColumn,applyColumn;

    private int currentNewsIndex = 0; // Initialize to 0
    private int newsPerPage = 3; // Number of news articles to display at a time

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ObservableList<News> newsItems = FXCollections.observableArrayList(News.getNews());

            // Initialize the dialog panes and images for the first news article
            populateNewsPanes(newsItems, currentNewsIndex);

            // Add an event handler for the "Next" button
            nextButton.setOnAction(event -> {
                if (currentNewsIndex + 1 < newsItems.size()-2) {
                    currentNewsIndex++;
                    populateNewsPanes(newsItems, currentNewsIndex);
                }
            });
            previousButton.setOnAction(event -> {
                if (currentNewsIndex - 1 < newsItems.size()-2) {
                    if(currentNewsIndex==0){
                        currentNewsIndex=1;
                    }
                    currentNewsIndex--;
                    populateNewsPanes(newsItems, currentNewsIndex);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        profileLink.setOnAction(this::handleProfileButtonClick);
        displayWeather();


        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        titleColumn.setMinWidth(425);
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
                    imageView.setFitWidth(255);
                    imageView.setFitHeight(150);

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

            // Add the user data pane to the StackPane
            userDataStackPane.getChildren().add(userDataPane);

            // Set the flag to indicate that the profile pane is open
            isProfilePaneOpen = true;
        } else {
            // Close the user data pane
            userDataStackPane.getChildren().clear();

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
            weatherPane.getChildren().addAll(temperatureLabel, conditionLabel,humidityLabel,percipLabel,windLabel,uvLabel, iconImageView);
        } else {
            System.out.println("Failed to fetch weather data.");
        }
    }
    // Implement this method to create and populate the user data pane
    private Pane createUserProfilePane() {
        // Create a Pane for user data
        Pane userDataPane = new Pane();

        // Populate the user data Pane with user-specific content
        Text userDataText = new Text("Name: " + User.getInstance().getFirstName()
                + " " + User.getInstance().getLastName()
                + "\nAddress: " + User.getInstance().toString()
                + "\nEmail: " + User.getInstance().getEmail()
                + "\nPhone number: " + User.getInstance().getPhoneNumber()
                + "\n\nUse role ID to check if a user is an admin or not.\n1 = User, 2 = Admin"
                + "\nUser role ID: " + User.getInstance().getRoleID());
        userDataText.setLayoutX(10);
        userDataText.setLayoutY(10);

        newCard = new Button("Add a New Payment Method"); // Replace with your user data components
        newCard.setLayoutX(10);
        newCard.setLayoutY(250);

        // Add an event handler to the button
        newCard.setOnAction(event -> loadBankFXML());

        userDataPane.getChildren().add(userDataText);
        userDataPane.getChildren().add(newCard);
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

}