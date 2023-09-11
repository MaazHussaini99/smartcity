package com.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LandingPageController implements Initializable {

    @FXML
    private ListView<String> newsListView; // Rename your ListView

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ObservableList<String> titles = FXCollections.observableArrayList();
            ObservableList<News> newsItems = FXCollections.observableArrayList(News.getNews()); // Rename your ObservableList

            for (News news : newsItems) {
                titles.add(news.getTitle());
                System.out.println(news.getTitle());
                System.out.println(news.getDescription());
                System.out.println(news.getUrl());
                System.out.println(news.getImg_url());
                System.out.println();

            }

            newsListView.setItems(titles); // Use the renamed ListView
        } catch (Exception e) {
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