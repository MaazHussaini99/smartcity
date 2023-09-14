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
                    "jdbc:mysql://smartcity-db2.ctpu1etrkqud.us-east-1.rds.amazonaws.com:3306/smartcity",
                    "admin", "maaz2023");
            return connection;
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }
}
