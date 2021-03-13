package com.belkin.finch_backend.exception.alreadyexist;

import com.belkin.finch_backend.exception.MyRestException;
import org.springframework.http.HttpStatus;

public class SelfSubscribeException extends MyRestException {
    public SelfSubscribeException() {
        super(HttpStatus.CONFLICT, "Self subscriptions are not allowed");
    }
}
