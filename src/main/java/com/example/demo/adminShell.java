package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/***
 * this class is used to interface with the databse without logging in
 * provide a text based implementation
 *
 */
public class adminShell {
    Connection connection;

    public adminShell() {
        connection = DBConn.connectDB();
        try {
            Job job = new Job(99999);
//            removeJob(job);
////            getJob(job);
            ArrayList<Job> jobs = getJobs();
            editJob(jobs.get(0),job);
            getAllJobs();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /***
     * get the list of jobs to display for admin, or fetch from job listing, idk
     * @return
     * @throws SQLException
     */
    public ArrayList<Job> getJobs() throws SQLException {
        String sql = "SELECT * FROM jobs";
        ArrayList<Job> jobs = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            jobs.add(new Job(resultSet.getString(2), resultSet.getInt(1),
                    resultSet.getString(3), resultSet.getString(4),
                    resultSet.getString(5)));
        }
        return jobs;

    }

    //remove job

    public void removeJob(Job job) throws SQLException {
        int jobID = job.getJobId();
        String sql = String.format("DELETE FROM jobs WHERE job_id=%s", jobID);
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.executeUpdate();
    }

    public void getJob(Job job) throws SQLException{
        int jobID = job.getJobId();
        String sql = String.format("SELECT * FROM jobs WHERE job_id = %s", jobID);
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
    }

    public void getAllJobs() throws SQLException {
        String sql = "SELECT * FROM jobs";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            for(int i =1;i<=5;i++){
                System.out.print (" " + rs.getString(i));
            }
            System.out.println();

        }
    }


    /***
     * this is fine
     * @param job
     * @throws SQLException
     */
    public void postJob(Job job) throws SQLException {
        String sql = String.format("INSERT INTO jobs VALUES ('%s','%s', '%s', '%s', '%s')",
                job.getJobId(),job.getJobTitle(),"","null",0,job.getJobAgency());
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.executeUpdate();
        getJob(job);
    }

    /***
     * replace an instance of old Job with new job
     * looks good
     * @param oldJob
     * @param newJob
     */
    public void editJob(Job oldJob, Job newJob) throws SQLException {
        removeJob(oldJob);
        postJob(newJob);
    }
    //edit job



}
