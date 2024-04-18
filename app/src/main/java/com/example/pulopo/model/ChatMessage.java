
package com.example.pulopo.model;

import java.util.Date;

public class ChatMessage {
    public String chatId;
    public String sendid, receivedid, mess, datetime;
    public Date dateObj;
    public int typeMess;
    public String getSendid() {
        return sendid;
    }

    public int getTypeMess() {
        return typeMess;
    }

    public void setTypeMess(int typeMess) {
        this.typeMess = typeMess;
    }

    public void setSendid(String sendid) {
        this.sendid = sendid;
    }

    public String getReceivedid() {
        return receivedid;
    }

    public void setReceivedid(String receivedid) {
        this.receivedid = receivedid;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Date getDateObj() {
        return dateObj;
    }

    public void setDateObj(Date dateObj) {
        this.dateObj = dateObj;
    }
}

