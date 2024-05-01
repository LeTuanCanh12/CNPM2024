
package com.example.pulopo.model.response;

public class SendMessResponse {
    private DataSend data;
    private boolean success;
    private String message;

    public DataSend getData() { return data; }
    public void setData(DataSend value) { this.data = value; }

    public boolean getSuccess() { return success; }
    public void setSuccess(boolean value) { this.success = value; }

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }
}


