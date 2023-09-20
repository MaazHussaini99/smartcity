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

    Connection connection=DBConn.connectDB();;
    @FXML
    AnchorPane anchorPane;

    VBox basePane;
    //behavior of the admin panel
    // it does two things,
    // promote/demote a user
    // send email to people
    // have a table of all users, and you can scroll through the users
    // and click on them to promote via a promote button
    // three rows name, emails, and isAdmin
    // there is also empty text fiedl that is disable until you click write email
    // this is below the empty text field, and will transform into 2 buttons
    // cancel and send
    // you can click on the people inthe rows to autopopulat who you will send it to
    //also I need a subject bar
    TableView<User> userTable;
    TextField emailTarget;
    TextField emailSubject;
    TextField emailContent;
    Button writeEmail;
    Button cancel;
    Button sendEmail;
    HBox buttonBox;
    Button back;

    static User user;
    public void initialize() {
        basePane = new VBox();
        buttonBox = new HBox();
        generateTable();
        addEmailFunction();
        anchorPane.getChildren().add(basePane);

    }


    public void addEmailFunction(){
        emailTarget = new TextField();
        emailSubject = new TextField();
        emailContent = new TextField();
        emailTarget.setPromptText("To:");
        emailSubject.setPromptText("Subject");
        emailContent.setPromptText("Write here!");
        writeEmail = new Button("Write Email");
        emailTarget.setDisable(true);
        emailSubject.setDisable(true);
        emailContent.setDisable(true);

        writeEmail.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonBox.getChildren().clear();
                enableWrite();
            }
        });

        addPromotionButton();
        addBackButton();
        buttonBox.getChildren().add(writeEmail);
        basePane.getChildren().addAll(emailTarget,emailSubject,emailContent,buttonBox);

    }

    public void addBackButton(){
        back = new Button("back");
        buttonBox.getChildren().add(back);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void enableWrite(){
        emailContent.setDisable(false);
        emailSubject.setDisable(false);
        emailTarget.setDisable(false);

        ArrayList<User> subjects = new ArrayList<>();
        //give table bonus function
        userTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User currentUser = userTable.getSelectionModel().getSelectedItem();
                String subjectLine ="";
                if(subjects.contains(currentUser)){
                    subjects.remove(currentUser);
                }
                else{
                    subjects.add(currentUser);
                }
                for(int i =0;i< subjects.size();i++){
                    subjectLine+=subjects.get(i).getEmail();
                    System.out.println(subjectLine + "i");
                    if(i!=subjectLine.length()-1){
                        subjectLine+=",";
                    }
                }
                emailTarget.setText(subjectLine);
            }
        });

        cancel = new Button("cancel");
        sendEmail = new Button("Send");
        buttonBox.getChildren().addAll(cancel,sendEmail);

        sendEmail.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sendEmail();
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonBox.getChildren().clear();
                basePane.getChildren().clear();
                addEmailFunction();

            }
        });

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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTarget.getText())); // Recipient's email address
            message.setSubject(emailSubject.getText());
            message.setText(emailContent.getText());

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }    }


    public void addPromotionButton(){
        Button promote = new Button("Promote a user");
        buttonBox.getChildren().add(promote);
        userTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                user = userTable.getSelectionModel().getSelectedItem();
                promote.setText(String.format("Promote %s %s.",user.getFirstName(),user.getLastName()));
            }
        });

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


        userTable = new TableView<>();
        userTable.getColumns().addAll(firstName,lastName,email,isAdmin);
        userTable.getItems().addAll(users);
        basePane.getChildren().add(userTable);
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
