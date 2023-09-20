package com.example.demo;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.sql.Connection;
/***
 * this class is used to interface with the databse without logging in
 * provide a text based implementation
 *
 */
public class adminShell {
    Connection connection;

    public adminShell() {
        final String username = "kevinzhengtwo@gmail.com"; // Your Gmail email address
        final String password = "iqly zzcf tqny taiv"; // Your Gmail password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("kevinvedi1999@gmail.com")); // Recipient's email address
            message.setSubject("Subject of your email");
            message.setText("Hello, This is the body of your email.");

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
