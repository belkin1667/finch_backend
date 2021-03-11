package com.belkin.finch_backend.exception.notfound;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException() {
        super("User");
    }

    public UserNotFoundException(String missingUsername) {
        super("User", "username", missingUsername);
    }
}
