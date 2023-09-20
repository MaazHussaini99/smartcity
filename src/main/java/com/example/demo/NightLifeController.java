package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class NightLifeController extends HotelBookingController {

    @FXML
    private Button nextButtonn,previousButtonn;
    private int currentNightLifeIndex = 0;

    @FXML
    private ImageView nightLifeImage1,nightLifeImage2,nightLifeImage3;
    @FXML
    private DialogPane nightLifedescriptionPane1,nightLifedescriptionPane2,nightLifedescriptionPane3;
    @FXML
    private Hyperlink nightLifeLink1,nightLifeLink2,nightLifeLink3;
    private int nightLifePerPage = 3;

    public void show() {
        try {
            ObservableList<NightLife> nightLifeItems = FXCollections.observableArrayList(NightLife.getNightlifeInfo());

            // Initialize the dialog panes and images for the first news article
            populateNightLifePanes(nightLifeItems, currentNightLifeIndex);

            // Add an event handler for the "Next" button
            nextButtonn.setOnAction(event -> {
                if (currentNightLifeIndex + 1 < nightLifeItems.size() - 2) {
                    currentNightLifeIndex++;
                    populateNightLifePanes(nightLifeItems, currentNightLifeIndex);
                }
            });
            previousButtonn.setOnAction(event -> {
                if (currentNightLifeIndex - 1 < nightLifeItems.size() - 2) {
                    if (currentNightLifeIndex == 0) {
                        currentNightLifeIndex = 1;
                    }
                    currentNightLifeIndex--;
                    populateNightLifePanes(nightLifeItems, currentNightLifeIndex);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ImageView getNightLifeImagePaneForIndex(int index) {
        switch (index) {
            case 0:
                return nightLifeImage1;
            case 1:
                return nightLifeImage2;
            case 2:
                return nightLifeImage3;
            default:
                // Handle any other index as needed
                return null;
        }
    }

    public DialogPane getNightLifeDescriptionPaneForIndex(int index) {
        switch (index) {
            case 0:
                return nightLifedescriptionPane1;
            case 1:
                return nightLifedescriptionPane2;
            case 2:
                return nightLifedescriptionPane3;
            default:
                // Handle any other index as needed
                return null;
        }
    }
    public Hyperlink getNightLifeLinkForIndex(int index) {
        switch (index) {
            case 0:
                return nightLifeLink1;
            case 1:
                return nightLifeLink2;
            case 2:
                return nightLifeLink3;
            // Add more cases if you have more Hyperlink fields in your layout
            default:
                throw new IllegalArgumentException("Invalid index: " + index);
        }
    }
    private void populateNightLifePanes(ObservableList<NightLife> nightLifeItems, int startIndex) {
        for (int i = 0; i < nightLifePerPage; i++) {
            int nightLifeIndex = startIndex + i;
            if (nightLifeIndex < nightLifeItems.size()) {
                NightLife nightLife = nightLifeItems.get(nightLifeIndex);
                ImageView imageView = getNightLifeImagePaneForIndex(i);

                DialogPane descriptionPane = getNightLifeDescriptionPaneForIndex(i);

                try {
                    if (nightLife.getImageUrl() == "null") {
                        Image image = new Image("file:src/main/resources/com/example/images/MicrosoftTeams-image.png");
                        imageView.setImage(image);
                    } else {
                        Image image = new Image(nightLife.getImageUrl());
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
                Hyperlink newsLink = getNightLifeLinkForIndex(i);
                newsLink.setText("Go to Source");
                newsLink.setUnderline(true);
                newsLink.setOnAction(event -> {
                    // Open the URL in a web browser when the hyperlink is clicked
                    String url = nightLife.getMoreInfoUrl();
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
                descriptionPane.setContentText(nightLife.getDescription());
                descriptionPane.setHeaderText(nightLife.getName());
            } else {
                // If there are no more news articles, clear the remaining panes
                ImageView imageView = getNightLifeImagePaneForIndex(i);
                DialogPane descriptionPane = getNightLifeDescriptionPaneForIndex(i);
                imageView.setImage(null);
                descriptionPane.setContentText("");
                descriptionPane.setHeaderText("");

            }
        }
    }
}
