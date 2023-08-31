package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConn {
    public static Connection connectDB(){
        Connection connection = null;
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/smartcity",
                    "root", "");

            // mydb is database
            // mydbuser is name of database
            // mydbuser is password of database

            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(
                    "select * from bank");
            int code;
            String title;
            while (resultSet.next()) {
                title = resultSet.getString("bank_name").trim();
                System.out.println(" Title : " + title);
            }
            resultSet.close();
            statement.close();
//            connection.close();
            return connection;
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }
}
