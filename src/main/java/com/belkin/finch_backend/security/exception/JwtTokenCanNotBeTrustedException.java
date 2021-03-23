package com.belkin.finch_backend.security.exception;

import com.belkin.finch_backend.exception.MyRestException;
import org.springframework.http.HttpStatus;

public class JwtTokenCanNotBeTrustedException extends MyRestException {
    public JwtTokenCanNotBeTrustedException(String token) {
        super(HttpStatus.UNAUTHORIZED, "Token '" + token + "' can not be trusted");
    }
}
