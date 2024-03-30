package com.codesteem.mylauncher.models;

/**
 * Represents user information for a given user in the MyLauncher app.
 */
public class UserInfo {

    // The URL or file path to the user's profile image
    private String userImage;

    // The user's name
    private String userName;

    // The user's contact details
    private ContactDetails contactDetails;

    // The user's social media handles
    private SocialMediaHandles socialMediaHandles;

    // A placeholder field for future use
    private String placeholder;

    /**
     * Constructs a new UserInfo object with the given user information.
     *
     * @param userImage        The URL or file path to the user's profile image
     * @param userName         The user's name
     * @param contactDetails   The user's contact details
     * @param socialMediaHandles The user's social media handles
     * @param placeholder      A placeholder field for future use
     */
    public UserInfo(String userImage, String userName, ContactDetails contactDetails,
                    SocialMediaHandles socialMediaHandles, String placeholder) {
        this.userImage = userImage;
        this.userName = userName;
        this.contactDetails = contactDetails;
        this.socialMediaHandles = socialMediaHandles;
        this.placeholder = placeholder;
    }

    /**
     * Returns the URL or file path to the user's profile image.
     *
     * @return The user's profile image
     */
    public String getUserImage() {
        return userImage;
    }

    /**
     * Returns the user's name.
     *
     * @return The user's name
     */
    public String getUserName() {
        return userName;

