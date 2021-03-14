package com.belkin.finch_backend.api.dto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@Getter @Setter
public class ExceptionResponse {
    String timestamp;
    Integer status;
    String error;
    String message;
    String path;

    public ExceptionResponse(Integer status, String error, String message, String path) {
        log.info("Creating ExceptionResponse from {\"status\": " + status + ", \"error\": " + error + ", \"message\": " + message + ", \"path\": " + path + "}");


        timestamp = OffsetDateTime.now(ZoneOffset.UTC).toString();
        this.status = status;
        this.error = error;
        this.message = message;
        if (path == null) {
            path = "";
        }
        this.path = path;

        log.info("Created ExceptionResponse: " + new Gson().toJson(this));
    }

    public ExceptionResponse(HttpStatus httpStatus, String message, String path) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), message, path);
    }
}