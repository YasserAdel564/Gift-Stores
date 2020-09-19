package com.gift.app.data.models;

import com.squareup.moshi.Json;

public class AuthResponse {

    @Json(name = "status")
    private Boolean status;
    @Json(name = "msg")
    private String msg;
    @Json(name = "data")
    private AuthModel data;

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

    public AuthModel getData() {
        return data;
    }

    public void setData(AuthModel data) {
        this.data = data;
    }
}


