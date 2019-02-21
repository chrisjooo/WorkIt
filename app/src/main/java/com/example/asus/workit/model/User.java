package com.example.asus.workit.model;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String dateofbirth;
    private int height;
    private int bodyweight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDateOfBirth() {
        return dateofbirth;
    }

    public void setDateOfBirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBodyWeight() {
        return bodyweight;
    }

    public void setBodyWeight(int bodyweight) {
        this.bodyweight = bodyweight;
    }
}