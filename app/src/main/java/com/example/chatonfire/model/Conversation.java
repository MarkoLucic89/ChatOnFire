package com.example.chatonfire.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class Conversation {
    @DocumentId
    private String id;
    private String recent_user_id;
    private String recent_user_name;
    private String recent_user_image;
    private String sender_id;
    private String receiver_id;
    private String last_message;
    private Date last_date;

    public Conversation() {
    }

    public Conversation(String sender_id, String receiver_id, String last_message, Date last_date) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.last_message = last_message;
        this.last_date = last_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecent_user_id() {
        return recent_user_id;
    }

    public void setRecent_user_id(String recent_user_id) {
        this.recent_user_id = recent_user_id;
    }

    public String getRecent_user_image() {
        return recent_user_image;
    }

    public void setRecent_user_image(String recent_user_image) {
        this.recent_user_image = recent_user_image;
    }

    public String getRecent_user_name() {
        return recent_user_name;
    }

    public void setRecent_user_name(String recent_user_name) {
        this.recent_user_name = recent_user_name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public Date getLast_date() {
        return last_date;
    }

    public void setLast_date(Date last_date) {
        this.last_date = last_date;
    }
}
