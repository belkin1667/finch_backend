package com.belkin.finch_backend.security.jwt.dto;

public class AuthenticationRequest {

    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
