package com.example.pulopo.model.response;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

public class DataSend {
    private UUID chatId;
    private long messageType;
    private Date dateSend;
    private String message;

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID value) {
        this.chatId = value;
    }

    public long getMessageType() {
        return messageType;
    }

    public void setMessageType(long value) {
        this.messageType = value;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date value) {
        this.dateSend = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }
}
