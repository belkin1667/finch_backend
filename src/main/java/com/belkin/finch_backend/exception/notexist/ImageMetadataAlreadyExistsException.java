package com.belkin.finch_backend.exception.notexist;

import com.belkin.finch_backend.exception.MyRestException;
import com.belkin.finch_backend.util.Base62;
import org.springframework.http.HttpStatus;

public class ImageMetadataAlreadyExistsException extends MyRestException {

    public ImageMetadataAlreadyExistsException(Base62 id) {
        this(id.getId());
    }

    public ImageMetadataAlreadyExistsException(String id) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Image Metadata with id " + id + " already exists"); //todo: correct Http status
    }
}
