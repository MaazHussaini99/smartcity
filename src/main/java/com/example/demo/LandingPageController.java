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
import java.util.List;
import java.util.ResourceBundle;

public class LandingPageController implements Initializable {

    @FXML
    private ListView<String> newsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> newsListData = News.getNewsTitles();
        String[] newsArray = newsListData.toArray(new String[0]);
        ObservableList<String> newsObservableList = FXCollections.observableArrayList(newsArray);
        newsList.setItems(newsObservableList);
    }
}