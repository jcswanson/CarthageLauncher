package com.codesteem.mylauncher.models;

public class UserInfo {
    private String userImage;
    private String userName;
    private String phone;
    private String email;
    private String whatsapp;
    private String instagram;
    private String messenger;
    private String snapchat;
    private String x;
    private String discordNow;

    public UserInfo(String userImage, String userName, String phone, String email, String whatsapp,
                    String instagram, String messenger, String snapchat, String x, String discordNow) {
        this.userImage = userImage;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.whatsapp = whatsapp;
        this.instagram = instagram;
        this.messenger = messenger;
        this.snapchat = snapchat;
        this.x = x;
        this.discordNow = discordNow;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWhatsApp() {
        return whatsapp;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getMessenger() {
        return messenger;
    }

    public String getSnapchat() {
        return snapchat;
    }

    public String getX() {
        return x;
    }

    public String getDiscordNow() {
        return discordNow;
    }
}

