package com.example.demo;

import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;

import java.net.URL;

public class MapController {


    public static void showMap(WebView webview){
//        WebView webView = new WebView();
//        WebEngine webEngine = webView.getEngine();
//
//        // Replace the URL with the Mapbox Static Tiles API URL for Albany, NY
//        String mapUrl = "https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/-73.7562,42.6526,14.5,0,0/800x600?access_token=pk.eyJ1IjoibWFhemh1c3NhaW5pIiwiYSI6ImNsbXJ2N3V2cTA5NXQya3FrNWN0N2hzNGkifQ.60aMjWuSQv4kTwPZzHi_Hg";
//
//        webEngine.load(mapUrl);



        WebEngine webEngine = webview.getEngine();
        webEngine.setJavaScriptEnabled(true);

// Get the local file path of the HTML file with double backslashes
        final URL htmlFilePath = MapController.class.getResource("map.html");
        webEngine.load(htmlFilePath.toExternalForm());

        webEngine.setOnError((WebErrorEvent event) -> {
            showErrorDialog(event.getMessage());
        });
    }

    private static void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("WebView Error");
        alert.setHeaderText("An error occurred while loading the web page.");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}

