package com.belkin.finch_backend.security.exception;

import com.belkin.finch_backend.exception.MyRestException;
import org.springframework.http.HttpStatus;

public class JwtTokenWasNotProvidedException extends MyRestException {
    public JwtTokenWasNotProvidedException() {
        super(HttpStatus.FORBIDDEN, "Token was not provided in 'Authorization' Header");
    }
}
