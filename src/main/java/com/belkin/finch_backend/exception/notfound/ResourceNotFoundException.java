package com.belkin.finch_backend.exception.notfound;

import com.belkin.finch_backend.exception.MyRestException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends MyRestException {

    public ResourceNotFoundException(String resource) {
        super(HttpStatus.NOT_FOUND, "Resource '" + resource + "' not found");
    }

    public ResourceNotFoundException(String resource, String attribute, String missingAttributeValue) {
        super(HttpStatus.NOT_FOUND, "Resource '" + resource + "' with '" + attribute + "=" + missingAttributeValue + "' not found");
    }
}
