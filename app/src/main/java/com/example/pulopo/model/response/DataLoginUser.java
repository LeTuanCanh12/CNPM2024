package com.example.pulopo.model.response;

public class DataLoginUser {
    private String password;
    private long id;
    private String hoTen;
    private String userName;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public long getid() {
        return id;
    }

    public void setid(long value) {
        this.id = value;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String value) {
        this.hoTen = value;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String value) {
        this.userName = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        this.email = value;
    }
}
