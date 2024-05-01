
package com.example.pulopo.model.response;

import java.util.List;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ChatByUserResponse {
    private List<DataChatByUser> data;
    private boolean success;
    private String message;

    public List<DataChatByUser> getData() {
        return data;
    }

    public void setData(List<DataChatByUser> value) {
        this.data = value;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean value) {
        this.success = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }
}



