package com.gift.app.data.models;

import com.squareup.moshi.Json;

public class CartModel {
    @Json(name = "id")
    private Integer id;
    @Json(name = "name")
    private String name;
    @Json(name = "price")
    private String price;
    @Json(name = "qty")
    private Integer qty;
    @Json(name = "photo")
    private String photo;

    @Json(name = "store_id")
    private Integer store_id;
    @Json(name = "department_id")
    private Integer department_id;

    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    public Integer getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Integer department_id) {
        this.department_id = department_id;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}


