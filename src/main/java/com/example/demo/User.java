package com.example.demo;

/**
 * User
 * Author: Owen Wurster
 */
public class User {

    private int roleID;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    private String zipcode;
    private String state;
    private String email;
    private String password;
    private String phoneNumber;

    public void createNewUser() {}

    public void createResumeProfile() {}

    // Getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getState() {
        return state;
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

    public int getRoleID() {
        return roleID;
    }

    // Setters
    // first_name, last_name, user_streetaddress, user_city, user_zipcode, state_ID, user_email, user_password, user_phone, role_ID

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStreetAddress(String address) {
        this.streetAddress = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}
