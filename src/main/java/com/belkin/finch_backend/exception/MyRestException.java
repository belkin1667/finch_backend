package com.belkin.finch_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
@AllArgsConstructor
public class MyRestException extends RuntimeException {
    private HttpStatus status;
    private String message;
}
