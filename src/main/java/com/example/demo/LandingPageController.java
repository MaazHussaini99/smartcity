package com.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LandingPageController implements Initializable {

    @FXML
    private ListView<String> newsList;

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