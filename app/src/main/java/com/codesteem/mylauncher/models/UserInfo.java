package com.codesteem.mylauncher.models;

/**
 * Represents user information for a given user in the MyLauncher app.
 */
public class UserInfo {
    // The URL or file path to the user's profile image
    private String userImage;
    
    // The user's name
    private String userName;
    
    // The user's phone number
    private String phone;
    
    // The user's email address
    private String email;
    
    // The user's WhatsApp number
    private String whatsapp;
    
    // The user's Instagram handle
    private String instagram;
    
    // The user's Messenger ID
    private String messenger;
    
    // The user's Snapchat handle
    private String snapchat;
    
    // A placeholder field for future use
    private String x;
    
    // The user's Discord handle
    private String discordNow;

    /**
     * Constructs a new UserInfo object with the given user information.
     *
     * @param userImage The URL or file path to the user's profile image
     * @param userName The user's name
     * @param phone The user's phone number
     * @param email The user's email address
     * @param whatsapp The user's WhatsApp number
     * @param instagram The user's Instagram handle
     * @param messenger The user's Messenger ID
     * @param snapchat The user's Snapchat handle
     * @param x A placeholder field for future use
     * @param discordNow The user's Discord handle
     */
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

    /**
     * Returns the URL or file path to the user's profile image.
     *
     *
