package com.belkin.finch_backend.api;

import com.belkin.finch_backend.api.dto.ExceptionResponse;
import com.belkin.finch_backend.exception.MyRestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = MyRestException.class)
    protected ResponseEntity<Object> handleMyRestException(MyRestException exception, HttpServletRequest request) {
        log.warn("4xx error occurred. Message: " + exception.getMessage() + ". Status: " + exception.getStatus().value() + " " + exception.getStatus().getReasonPhrase());


        ExceptionResponse apiError = new ExceptionResponse(exception.getStatus(), exception.getMessage(), request.getServletPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    protected ResponseEntity<Object> handleMyRestException(AuthenticationException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        log.warn("4xx Authentication error occurred. Message: " + exception.getMessage() + ". Status: " + status.value() + " " + status.getReasonPhrase());

        ExceptionResponse apiError = new ExceptionResponse(status, exception.getMessage(), request.getServletPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleMyRestException(Exception exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("5xx error occurred. Message: " + exception.getMessage() + ". Status: " + status.value() + " " + status.getReasonPhrase());

        ExceptionResponse apiError = new ExceptionResponse(status, exception.getMessage(), request.getServletPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}

