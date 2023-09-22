package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * A list of useful method
 * often a bit too late
 */
public class SQLHelper {


    public static ResultSet makeQuery(String sql) {
        ResultSet rs = null;
        try {

            Connection connection = DBConn.connectDB();
            PreparedStatement selectStatement = connection.prepareStatement(sql);
            rs = selectStatement.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.print(e.getStackTrace());
        }
        return rs;
    }

    public static void deleteQuery(String sql){
        try {

            Connection connection = DBConn.connectDB();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeQuery();
            System.out.print(sql+ " Executed");
        } catch (SQLException e) {
            System.out.print(e.getStackTrace());
        }
    }
}
