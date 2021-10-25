package com.example.chatonfire.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class ChatMessage {
    @DocumentId
    private String id;
    private String name;
    private String image;
    private String sender_id;
    private String receiver_id;
    private String message;
    private Date date_object;

    public ChatMessage() {
    }

    public ChatMessage(String sender_id, String receiver_id, String message, Date date_object) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message = message;
        this.date_object = date_object;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate_object() {
        return date_object;
    }

    public void setDate_object(Date date_object) {
        this.date_object = date_object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
