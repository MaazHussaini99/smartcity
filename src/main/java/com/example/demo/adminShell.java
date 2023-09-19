package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;

import org.simplejavamail.*;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

/***
 * this class is used to interface with the databse without logging in
 * provide a text based implementation
 *
 */
public class adminShell {
    Connection connection;

    public adminShell() {
        connection = DBConn.connectDB();
        System.out.println("Send Emails");
        Email email = EmailBuilder.startingBlank().from("kevinzheng","kevinvedi1999@gmail.com")
                .to("kevin","kevinvedi1999@gmail.com")
                .withSubject("This is a email")
                .withPlainText("The opening of our ceremony!!!").buildEmail();
        MailerBuilder.withSMTPServer("mail.smtp2go.com",2525,"bob.the.builder","yeswecan").buildMailer().sendMail(email);
    }

    public void sendEmail(){
    }


}
