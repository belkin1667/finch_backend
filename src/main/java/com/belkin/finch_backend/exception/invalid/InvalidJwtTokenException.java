package com.belkin.finch_backend.exception.invalid;

import com.belkin.finch_backend.exception.MyRestException;
import org.springframework.http.HttpStatus;

public class InvalidJwtTokenException extends MyRestException {
    public InvalidJwtTokenException(String token) {
        super(HttpStatus.BAD_REQUEST, "JWT token '" + token +"' is invalid");
    }
}
