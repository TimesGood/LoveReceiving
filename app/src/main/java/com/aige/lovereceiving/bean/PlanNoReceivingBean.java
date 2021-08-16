package com.aige.lovereceiving.bean;

public class PlanNoReceivingBean {
    private String code;
    private String data;
    private String msg;
    private int count;
    private String other;
    private int responseCode = 200;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "PlanNoReceivingBean{" +
                "code='" + code + '\'' +
                ", data='" + data + '\'' +
                ", msg='" + msg + '\'' +
                ", count=" + count +
                ", other='" + other + '\'' +
                ", responseCode=" + responseCode +
                '}';
    }
}
