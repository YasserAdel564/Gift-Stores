package com.gift.app.data.models;

import com.squareup.moshi.Json;

import java.util.List;

public class CartResponse {

    @Json(name = "status")
    private Boolean status;
    @Json(name = "msg")
    private String msg;
    @Json(name = "total_price")
    private String total_price;
    @Json(name = "delivery_price")
    private String delivery_price;
    @Json(name = "all")
    private String all;
    @Json(name = "data")
    private List<CartModel> data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public List<CartModel> getData() {
        return data;
    }

    public void setData(List<CartModel> data) {
        this.data = data;
    }
}


