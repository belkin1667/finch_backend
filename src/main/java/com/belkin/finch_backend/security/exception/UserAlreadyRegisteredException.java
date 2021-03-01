package com.belkin.finch_backend.security.exception;


import org.springframework.security.core.AuthenticationException;

public class UserAlreadyRegisteredException extends AuthenticationException {

    public UserAlreadyRegisteredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserAlreadyRegisteredException(String msg) {
        super(msg);
    }
}
