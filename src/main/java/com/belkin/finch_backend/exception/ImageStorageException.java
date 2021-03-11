package com.belkin.finch_backend.exception;

import org.springframework.http.HttpStatus;

public class ImageStorageException extends MyRestException {

    public ImageStorageException(String filename) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store image " + filename + ". Please try again");
    }
}
