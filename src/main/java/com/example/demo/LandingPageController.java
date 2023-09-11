package com.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.net.URL;
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


}