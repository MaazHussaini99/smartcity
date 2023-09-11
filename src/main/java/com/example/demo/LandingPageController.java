package com.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LandingPageController implements Initializable {

    @FXML
    private ListView<String> newsList;
    @FXML
    private Button profileLink;
    @FXML
    private void onProfileLinkClicked() {
        loadBankFXML();
    }
    private void loadBankFXML() {
        try {
            // Load the Bank.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bank.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) profileLink.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<News> newsList = new ArrayList<>();
            newsList = News.getNews();
            for(News news : newsList){
                System.out.println(news.getTitle());
                System.out.println(news.getDescription());
                System.out.println(news.getUrl());
                System.out.println(news.getImg_url());
                System.out.println();
            }
//            ObservableList<String> names = FXCollections.observableArrayList(News.getNewsTitles());
//            newsList.setItems(names);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}