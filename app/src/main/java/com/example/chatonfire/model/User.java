package com.example.chatonfire.model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class User implements Serializable {

    @DocumentId
    private String user_id;
    private String user_name;
    private String user_email;
    private String user_password;
    private String user_image;
    private String user_status;

    public User() {
    }

    public User(String user_name, String user_email, String user_password, String user_image) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_image = user_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
}
