package com.belkin.finch_backend.exception.invalid;

import com.belkin.finch_backend.exception.MyRestException;
import com.belkin.finch_backend.util.Base62;
import org.springframework.http.HttpStatus;

public class InvalidBase62Exception extends MyRestException {

    public InvalidBase62Exception(String base62) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, base62 + " is not in base62 encoding");
    }

    public InvalidBase62Exception(Base62 base62) {
        this(base62.toString());
    }
}
