package com.example.pulopo.model.response;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

public class DataChatByUser {
    private long senderId;
    private long receiverId;
    private String chatId;
    private long messageType;
    private String dateSend;
    private String message;

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long value) {
        this.senderId = value;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long value) {
        this.receiverId = value;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String value) {
        this.chatId = value;
    }

    public long getMessageType() {
        return messageType;
    }

    public void setMessageType(long value) {
        this.messageType = value;
    }

    public String getDateSend() {
        return dateSend;
    }

    public void setDateSend(String value) {
        this.dateSend = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }
}