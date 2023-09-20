module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires java.desktop;
    requires javafx.web;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}