package com.belkin.finch_backend.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenCanNotBeTrusted extends AuthenticationException {
    public JwtTokenCanNotBeTrusted(String token) {
        super("Token '" + token + "' can not be trusted");
    }
}
