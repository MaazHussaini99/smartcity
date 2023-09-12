package com.example.demo;

public class Job {
    private String jobTitle;
    private int jobId;
    private String jobGrade;
    private String jobAgency;
    private String jobLocation;

    public Job(String jobTitle, int jobId, String jobGrade, String jobAgency, String jobLocation){
        this.jobTitle = jobTitle;
        this.jobId = jobId;
        this.jobGrade = jobGrade;
        this.jobAgency = jobAgency;
        this.jobLocation = jobLocation;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public int getJobId() {
        return jobId;
    }

    public String getJobGrade() {
        return jobGrade;
    }

    public String getJobAgency() {
        return jobAgency;
    }

    public String getJobLocation() {
        return jobLocation;
    }

}
