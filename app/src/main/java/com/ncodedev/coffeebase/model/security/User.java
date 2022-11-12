package com.ncodedev.coffeebase.model.security;

public class User {

    private static User instance;

    private String userId;

    private String username;

    private String email;

    private String pictureUri;

    private String token;

    public User(final String userId, final String username, final String email, final String pictureUri) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.pictureUri = pictureUri;
        instance = this;
    }

    private User() { }

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

    public void setToken(final String token) {
        this.token = token;
    }
}
