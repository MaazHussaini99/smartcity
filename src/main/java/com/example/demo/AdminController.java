package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    private TableView<JobApplication> jobApplications;
    @FXML
    private TextField emailTarget;
    @FXML
    private TextField emailSubject;
    @FXML
    private TextField emailContent;
    @FXML
    private Button sendEmailButton,back,accept,reject,promote;

    JobApplication selectedApplication;
    static User user;
    public void initialize() {
        // Initialize your UI components and set event handlers here.
        // The UI components are already injected via @FXML annotations.
        generateTable();
        addEmailFunction();
        fillJobApplicationTable();
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
        acceptOrDenyApplication();
        setJobTableBehavior();
    }

    public void setJobTableBehavior() {

        jobApplications.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedApplication = jobApplications.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void acceptOrDenyApplication(){
        String delete = "DELETE FROM jobapplications WHERE app_id=";

        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Clippy.deleteQuery(delete+selectedApplication.jbID);
                jobApplications.getItems().remove(selectedApplication);
                System.out.println("Accepted~!");
                //todo make a pop up
            }
        });

        reject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Clippy.deleteQuery(delete+selectedApplication.jbID);
                jobApplications.getItems().remove(selectedApplication);
                System.out.println("rejected~!");
                //todo make a pop up
            }
        });


    }

    public void fillJobApplicationTable(){
        ObservableList<JobApplication> jobApplicationsList= FXCollections.observableArrayList(getJobApplication());
        TableColumn<JobApplication,String> name = new TableColumn("Name");
        TableColumn<JobApplication,String> jobDescription = new TableColumn("Job");
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        jobDescription.setCellValueFactory(cellData-> cellData.getValue().jobProperty());

        jobApplications.getColumns().addAll(name,jobDescription);
        jobApplications.getItems().addAll(jobApplicationsList);
    }

    public ArrayList<JobApplication> getJobApplication(){
        ArrayList<User> users = getUser();
        ArrayList<Job> jobs =  new ArrayList<>(JobListing.jobs);
        ArrayList<JobApplication> applications = new ArrayList<>();
        String sql = "SELECT * FROM jobapplication";
        ResultSet rs = Clippy.makeQuery(sql);
            try {
                while(rs.next()){
                    int applicationUserID = rs.getInt(3);
                    int applicationJobID = rs.getInt(2);
                    System.out.println(applicationJobID+" "+applicationJobID);

                    User user = null;
                    Job job = null;
                    for(int i =0;i<users.size();i++){
                        int userID = users.get(i).getUserID();
                        if(applicationUserID == userID){
                            user = users.get(i);
                        }
                    }
                    for(int i =0;i<jobs.size();i++){
                        int jobID = jobs.get(i).getJobId();
                        if(jobID == applicationJobID){
                            job = jobs.get(i);
                        }
                    }
                    applications.add(new JobApplication(user,job,rs.getInt(1)));

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return applications;
    }


    class JobApplication{
        User user;
        Job job;

        int jbID;

        public JobApplication(User user, Job job, int id){
            this.user   = user;
            this.job = job;
            jbID = id;
        }

        public StringProperty nameProperty(){
            return new SimpleStringProperty(user.getFirstName()+" "+user.getLastName());
        }

        public StringProperty jobProperty(){
            return new SimpleStringProperty(job.getJobTitle());
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
