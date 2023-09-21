package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

import static com.example.demo.JobListing.jobs;

public class AdminController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TextField emailTarget;
    @FXML
    private TextField emailSubject;
    @FXML
    private TextField emailContent;
    @FXML
    private Button sendEmailButton,back,accept,reject,promote;
    static User user;
    public void initialize() {
        // Initialize your UI components and set event handlers here.
        // The UI components are already injected via @FXML annotations.
        generateTable();
        addEmailFunction();
    }


    public void addEmailFunction(){
        emailTarget = new TextField();
        emailSubject = new TextField();
        emailContent = new TextField();
        emailTarget.setPromptText("To:");
        emailSubject.setPromptText("Subject");
        emailContent.setPromptText("Write here!");
        emailTarget.setDisable(true);
        emailSubject.setDisable(true);
        emailContent.setDisable(true);


    }

    public void BackButton(){
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {


                    // Load the LandingPage.fxml file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("landing-page.fxml"));
                    Parent root = loader.load();

                    // Create a new scene
                    Scene scene = new Scene(root);

                    // Get the current stage and set the new scene
                    Stage stage = (Stage) anchorPane.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    stage.centerOnScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    public void enableWrite(){
//        emailContent.setDisable(false);
//        emailSubject.setDisable(false);
//        emailTarget.setDisable(false);
//
//        ArrayList<User> subjects = new ArrayList<>();
//        //give table bonus function
//        userTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                User currentUser = userTable.getSelectionModel().getSelectedItem();
//                String subjectLine ="";
//                if(subjects.contains(currentUser)){
//                    subjects.remove(currentUser);
//                }
//                else{
//                    subjects.add(currentUser);
//                }
//                for(int i =0;i< subjects.size();i++){
//                    subjectLine+=subjects.get(i).getEmail();
//                    System.out.println(subjectLine + "i");
//                    if(i!=subjectLine.length()-1){
//                        subjectLine+=",";
//                    }
//                }
//                emailTarget.setText(subjectLine);
//            }
//        });
//
//        cancel = new Button("cancel");
//        sendEmail = new Button("Send");
//        buttonBox.getChildren().addAll(cancel,sendEmail);
//
//        sendEmail.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                sendEmail();
//            }
//        });
//
//        cancel.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                buttonBox.getChildren().clear();
//                basePane.getChildren().clear();
//                addEmailFunction();
//
//            }
//        });
//
//    }

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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTarget.getText())); // Recipient's email address
            message.setSubject(emailSubject.getText());
            message.setText(emailContent.getText());

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }    }


    public void PromotionButton(){
        promote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(user == null){
                    return;
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText(String.format("Are your sure you want to make %s %s admin?",user.getFirstName(),user.getLastName()));
                alert.setTitle("Are you sure?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    System.out.println("Yes");
                    String sql = "UPDATE user SET role_ID = 2 WHERE uid=  "+user.getUserID();

                    try{
                        Connection connection = DBConn.connectDB();
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.executeUpdate();
                    }
                    catch(SQLException e){
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    public void generateTable(){
        //get users
        ObservableList<User> users = FXCollections.observableArrayList(getUser());
        TableColumn<User,String> firstName = new TableColumn("First Name");
        TableColumn <User,String>lastName = new TableColumn("Last Name");
        TableColumn<User,String> email = new TableColumn("Email");
        TableColumn<User,String> isAdmin = new TableColumn("Admin");

        firstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        isAdmin.setCellValueFactory(cellData -> cellData.getValue().adminProperty());


        userTable.getColumns().addAll(firstName,lastName,email,isAdmin);
        userTable.getItems().addAll(users);
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
                System.out.printf("%s %s %s %s \n",resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(8),
                        resultSet.getInt(11));

            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
