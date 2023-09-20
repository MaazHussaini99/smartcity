package com.example.demo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        makeAdmin();
    }


    public void makeAdmin(){
        String sql ="UPDATE user SET role_ID = 2 WHERE uid = 2";
        try{
      connection = DBConn.connectDB();
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.executeUpdate();
      getUser();

  }catch (SQLException e){
      throw new RuntimeException(e);
  }

    }
    public ArrayList<User> getUser(){
        String sql = "SELECT * FROM user";
        ArrayList<User> users = new ArrayList<>();
        try {
            Connection connection = DBConn.connectDB();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                users.add(new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(8),
                        resultSet.getInt(11)
                ));
                System.out.printf("%s %s %s %s %s \n",
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(8),resultSet.getInt(11));

            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(){

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
        }    }

}
