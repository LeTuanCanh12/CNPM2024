package com.example.pulopo.model.response;

import com.example.pulopo.model.User;

import java.util.List;

public class SearchUserResponse {
    private List<User> data;
    private boolean success;
    private String message;

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
