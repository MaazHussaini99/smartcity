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

    /**
     * private User constructor
     * Only called in initializeUser when user is null
     *
     * @param firstName first name
     * @param lastName last name
     * @param streetAddress street address
     * @param city city
     * @param zipcode zipcode
     * @param state state
     * @param email email
     * @param password password
     * @param phoneNumber phone number
     * @param roleID role ID
     */
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

    /**
     * initializeUser
     * Used to initialize user if the object is null
     *
     * @param firstName first name
     * @param lastName last name
     * @param streetAddress street address
     * @param city city
     * @param zipcode zipcode
     * @param state state
     * @param email email
     * @param password password
     * @param phoneNumber phone number
     * @param roleID role ID
     * @return a User object
     */
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

    /**
     * logOut
     * Sets the user object to null when the user logs out
     */
    public static void logOut() {
        user = null;
    }

    /**
     * getInstance
     * Returns a user object to be interacted with by the rest of the application
     *
     * @return a user object
     */
    public static synchronized User getInstance() {
        return user;
    }

    //public void createResumeProfile() {}

    // Getters

    /**
     * getFirstName
     * Returns the user's first name
     * @return String firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * getLastName
     * Returns the user's last name
     * @return String lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * getStreetAddress
     * Returns the user's street address
     * @return String streetAddress
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * getCity
     * Returns the city that the user lives in
     * @return String city
     */
    public String getCity() {
        return city;
    }

    /**
     * getZipcode
     * Return's the user's zipcode
     * @return String zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * getState
     * Returns the state that the user lives in
     * @return String state
     */
    public String getState() {
        return state;
    }

    /**
     * getEmail
     * Returns the user's email
     * @return String email
     */
    public String getEmail() {
        return email;
    }

    /**
     * getPassword
     * Returns the user's password
     * @return String password
     */
    public String getPassword() {
        return password;
    }

    /**
     * getPhoneNumber
     * Returns the user's phone number
     * @return String phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * getRoleID
     * Returns the user's role
     * @return int roleID
     */
    public int getRoleID() {
        return roleID;
    }

    // Setters

    /**
     * setFirstName
     * Sets the user's first name
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * setLastName
     * Sets the user's last name
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * setStreetAddress
     * Sets the user's street address
     * @param address street address
     */
    public void setStreetAddress(String address) {
        this.streetAddress = address;
    }

    /**
     * setCity
     * Sets the city that the user lives in
     * @param city city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * setZipcode
     * Sets the user's zipcode
     * @param zipcode zipcode
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * setState
     * Sets the state that the user lives in
     * @param state state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * setEmail
     * Sets the user's email
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * setPassword
     * Sets the user's password
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * setPhoneNumber
     * Sets the user's phone number
     * @param phoneNumber phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * setRoleID
     * Sets the user's role ID
     * @param roleID
     */
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}
