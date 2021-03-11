package com.belkin.finch_backend.security.exception;

import com.belkin.finch_backend.exception.MyRestException;
import org.springframework.http.HttpStatus;

public class UserAlreadyRegisteredException extends MyRestException {

    public UserAlreadyRegisteredException(String msg) {
        super(HttpStatus.CONFLICT, "'" + msg + "' is already registered username");
    }
}
