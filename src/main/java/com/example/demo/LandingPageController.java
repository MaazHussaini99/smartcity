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
    private ListView<News> newsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ObservableList<News> names = FXCollections.observableArrayList(News.getNews());
            News[] newsArray = new News[names.size()];
            names.toArray(newsArray);

            for (News news : newsArray) {
                System.out.println(news.getTitle());
                System.out.println(news.getDescription());
                System.out.println(news.getUrl());
                System.out.println(news.getImg_url());
                System.out.println();
            }

            newsList.setItems(names);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}