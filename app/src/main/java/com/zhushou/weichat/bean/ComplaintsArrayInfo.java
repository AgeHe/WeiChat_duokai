package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/9/29.
 */

public class ComplaintsArrayInfo {
    private String date = "";//时间
    private String id= "";//投诉号
    private String status= "";//状态
    private String response= "";//回复

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
