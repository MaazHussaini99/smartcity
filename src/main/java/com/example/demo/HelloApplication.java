package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("logged-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 544, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
<<<<<<< HEAD
        News.getNews();
=======


        DBConn newDB = new DBConn();
        newDB.DBConn();

>>>>>>> Dax
    }

    public static void main(String[] args) {
        launch();
    }
}