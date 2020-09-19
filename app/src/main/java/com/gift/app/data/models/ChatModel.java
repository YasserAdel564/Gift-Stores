package com.gift.app.data.models;

import com.squareup.moshi.Json;

public class ChatModel {

    @Json(name = "id")
    private Integer id;
    @Json(name = "message")
    private String message;
    @Json(name = "sender")
    private String sender;
    @Json(name = "photo")
    private String photo;
    @Json(name = "user_name")
    private String user_name;
    @Json(name = "date")
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
