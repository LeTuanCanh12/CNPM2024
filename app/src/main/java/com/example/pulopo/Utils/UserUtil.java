package com.example.pulopo.Utils;

public class UserUtil {
    public static int id;
    public static String userName = "";

    public static String password = "";
    public static String hoTen = "";
    public static String email = "";
    public static String senderImg = "";
    public UserUtil() {
    }

    public static String getSenderImg() {
        return senderImg;
    }

    public static void setSenderImg(String senderImg) {
        UserUtil.senderImg = senderImg;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        UserUtil.id = id;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserUtil.userName = userName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserUtil.password = password;
    }

    public static String getHoTen() {
        return hoTen;
    }

    public static void setHoTen(String hoTen) {
        UserUtil.hoTen = hoTen;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserUtil.email = email;
    }
}
