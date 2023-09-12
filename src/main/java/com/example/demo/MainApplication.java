package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("logged-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 544, 400);
        stage.setTitle("Smart City Application");
        stage.setScene(scene);
        stage.show();
        JobListing.getAllJobs();
    }

    public static void main(String[] args) {
        launch();
    }
}