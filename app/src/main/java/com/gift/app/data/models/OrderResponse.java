package com.gift.app.data.models;

import com.squareup.moshi.Json;

import java.util.List;

public class OrderResponse {
    @Json(name = "status")
    private Boolean status;
    @Json(name = "msg")
    private String msg;

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
}
