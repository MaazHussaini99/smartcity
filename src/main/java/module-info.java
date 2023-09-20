module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires java.desktop;
    requires org.simplejavamail;
    requires org.simplejavamail.core;
    requires java.mail;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}