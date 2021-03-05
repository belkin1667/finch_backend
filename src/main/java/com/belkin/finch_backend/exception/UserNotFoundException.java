package com.belkin.finch_backend.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String missingUsername) {
        super("Username " + missingUsername + " not found");
    }
}
