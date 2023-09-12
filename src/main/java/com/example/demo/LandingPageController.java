package com.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LandingPageController extends HotelBookingController implements Initializable {

    @FXML
    private ListView<String> newsListView; // Rename your ListView
    @FXML
    private DialogPane descriptionPane;
    @FXML
    private Hyperlink linkHyperlink;
    @FXML
    private ImageView newsImage;
    @FXML
    private Button profileLink;
    @FXML
    private StackPane userDataStackPane;
    private boolean isProfilePaneOpen = false;
    private ListView<String> newsList;
    @FXML
    private Button newCard;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ObservableList<String> titles = FXCollections.observableArrayList();
            ObservableList<News> newsItems = FXCollections.observableArrayList(News.getNews());
            for (News news : newsItems) {
                titles.add(news.getTitle());

            }
            newsListView.setItems(titles);

            // Add an event handler for the ListView
            newsListView.setOnMouseClicked(event -> {
                String selectedTitle = newsListView.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {
                    // Find the corresponding News object
                    News selectedNews = null;
                    for (News news : newsItems) {
                        if (news.getTitle().equals(selectedTitle)) {
                            selectedNews = news;
                            break;
                        }
                    }

                    // Display the news article in the articlePane (you need to implement this)
                    displayNewsArticle(selectedNews);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        profileLink.setOnAction(this::handleProfileButtonClick);
    }
    private void displayNewsArticle(News news) {
        // Set the title, description, and image of News article
        if(news.getImg_url() == "null" || news.getImg_url() == ""){
            descriptionPane.setContentText(news.getDescription());
            descriptionPane.setHeaderText(news.getTitle());
        }else {
            Image image = new Image(news.getImg_url());
            newsImage.setImage(image);
            descriptionPane.setContentText(news.getDescription());
            descriptionPane.setHeaderText(news.getTitle());
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

    // Implement this method to create and populate the user data pane
    private Pane createUserProfilePane() {
        // Create a Pane for user data
        Pane userDataPane = new Pane();

        // Populate the user data Pane with user-specific content
        Text userDataText = new Text("User Profile Data"); // Replace with your user data components
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
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("logged-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 544, 400);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("Smart City - Sign up");
        stage.setScene(scene);
        stage.show();
    }

}