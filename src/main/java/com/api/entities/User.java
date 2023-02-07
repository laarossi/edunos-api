package com.api.entities;

import java.sql.Date;
import java.sql.ResultSet;

public class User extends Entity{

    private boolean admin;
    private String authenticationKey;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String location;
    private Date birthDate;
    private String facebook;
    private String twitter;
    private String github;
    private String picture;
    private Date registrationDate;

    public User(){
        super(0);
    }

    public User(int id, boolean admin, String username, String email, String password, String fullName, String location, Date birthDate, String facebook, String twitter, String github, String picture, Date registrationDate) {
        super(id);
        this.admin = admin;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.location = location;
        this.birthDate = birthDate;
        this.facebook = facebook;
        this.twitter = twitter;
        this.github = github;
        this.picture = picture;
        this.registrationDate = registrationDate;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

}
