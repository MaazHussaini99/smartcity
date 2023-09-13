package com.example.demo;

/**
 * User
 * Author: Owen Wurster
 */
public class User {

    private static User user = null;

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

    private User(String firstName,
                String lastName,
                String streetAddress,
                String city,
                String zipcode,
                String state,
                String email,
                String password,
                String phoneNumber,
                int roleID) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roleID = roleID;
    }

    public static synchronized User initializeUser(String firstName,
                                                String lastName,
                                                String streetAddress,
                                                String city,
                                                String zipcode,
                                                String state,
                                                String email,
                                                String password,
                                                String phoneNumber,
                                                int roleID) {
        if (user == null)
            user = new User(firstName,
                    lastName,
                    streetAddress,
                    city,
                    zipcode,
                    state,
                    email,
                    password,
                    phoneNumber,
                    roleID);

        return user;
    }

    public static void logOut() {
        user = null;
    }

    public static synchronized User getInstance() {
        return user;
    }

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
