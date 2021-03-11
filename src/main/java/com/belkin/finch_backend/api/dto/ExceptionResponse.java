package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.exception.MyRestException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter @Setter
public class ExceptionResponse {
    String timestamp;
    Integer status;
    String error;
    String message;
    String path;

    public ExceptionResponse(Integer status, String error, String message, String path) {
        timestamp = OffsetDateTime.now(ZoneOffset.UTC).toString();
        this.status = status;
        this.error = error;
        this.message = message;
        if (path == null)
            path = "";
        this.path = path;
    }

    public ExceptionResponse(HttpStatus httpStatus, String message, String path) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), message, path);
    }
}