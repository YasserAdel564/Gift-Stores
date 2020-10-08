package com.gift.app.data.models;

import com.squareup.moshi.Json;

import java.util.List;

public class DepartmentsResponse {


    @Json(name = "status")
    private Boolean status;
    @Json(name = "msg")
    private String msg;
    @Json(name = "open_from")
    private String open_from;
    @Json(name = "open_to")
    private String open_to;
    @Json(name = "open")
    private Boolean open;
    @Json(name = "data")
    private List<Department> data ;


    public String getOpen_from() {
        return open_from;
    }

    public void setOpen_from(String open_from) {
        this.open_from = open_from;
    }

    public String getOpen_to() {
        return open_to;
    }

    public void setOpen_to(String open_to) {
        this.open_to = open_to;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }



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

    public List<Department> getData() {
        return data;
    }

    public void setData(List<Department> data) {
        this.data = data;
    }
}
