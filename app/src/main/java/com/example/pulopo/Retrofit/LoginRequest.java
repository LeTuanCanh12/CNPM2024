package com.example.pulopo.Retrofit;

import com.google.gson.annotations.SerializedName;

public  class LoginRequest {
    @SerializedName("userName")
    private String userName;

    @SerializedName("password")
    private String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
