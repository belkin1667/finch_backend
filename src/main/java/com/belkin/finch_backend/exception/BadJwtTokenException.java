package com.belkin.finch_backend.exception;

import org.springframework.http.HttpStatus;

public class BadJwtTokenException extends MyRestException {
    public BadJwtTokenException(String token) {
        super(HttpStatus.BAD_REQUEST, "JWT token '" + token +"' is invalid");
    }
}
