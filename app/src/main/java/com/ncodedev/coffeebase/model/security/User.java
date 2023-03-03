package com.ncodedev.coffeebase.model.security;

public class User {

    private static User instance;

    private final String userId;

    private final String username;

    private final String email;

    private final String pictureUri;

    private final String token;

    public User(final String userId, final String username, final String email, final String pictureUri, String token) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.pictureUri = pictureUri;
        this.token = token;
        instance = this;
    }

    public static User getInstance() {
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public String getToken() {
        return token;
    }

    public static void clearUserData() {
        instance = null;
    }
}
