package com.aige.lovereceiving.bean;

public class UserBean {
    private String id;
    private String username;
    private String orderpre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrderpre() {
        return orderpre;
    }

    public void setOrderpre(String orderpre) {
        this.orderpre = orderpre;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", orderpre='" + orderpre + '\'' +
                '}';
    }
}
