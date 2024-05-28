// ChatInforResponse.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.pulopo.model.response;
import java.sql.Timestamp;

public class ChatInforResponse {
    private DataChatInfor data;
    private boolean success;
    private String message;

    public DataChatInfor getData() { return data; }
    public void setData(DataChatInfor value) { this.data = value; }

    public boolean getSuccess() { return success; }
    public void setSuccess(boolean value) { this.success = value; }

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }
}

// Data.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation


class DataChatInfor {
    private String chatId;
    private long messageType;
    private Timestamp dateSend;
    private String message;

    public String getChatId() { return chatId; }
    public void setChatId(String value) { this.chatId = value; }

    public long getMessageType() { return messageType; }
    public void setMessageType(long value) { this.messageType = value; }

    public Timestamp getDateSend() { return dateSend; }
    public void setDateSend(Timestamp value) { this.dateSend = value; }

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }
}
