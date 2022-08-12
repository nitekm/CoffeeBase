package com.ncodedev.coffeebase.model.security;

public class Token {

    private String token;

    public Token(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}
