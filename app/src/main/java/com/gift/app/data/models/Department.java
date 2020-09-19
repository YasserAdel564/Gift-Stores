package com.gift.app.data.models;

import com.squareup.moshi.Json;

public class Department {


    @Json(name = "id")
    private Integer id;
    @Json(name = "name")
    private String name;
    @Json(name = "photo")
    private String photo;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}


