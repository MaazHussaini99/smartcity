package com.example.demo;

/**
 * User
 * Author: Owen Wurster
 */
public class User {

    private int userID;
    private int roleID;
    private int jobID;
    private char middleInitial;
    private String firstName;
    private String lastName;
    private String address;
    private String password;
    private String email;
    private String phoneNumber;

    public void createNewUser() {}

    public void createResumeProfile() {}

    // Setters
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public void setMiddleInitial(char middleInitial) {
        this.middleInitial = middleInitial;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public int getUserID() {
        return userID;
    }

    public int getRoleID() {
        return roleID;
    }

    public int getJobID() {
        return jobID;
    }

    public char getMiddleInitial() {
        return middleInitial;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
