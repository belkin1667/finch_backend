package com.belkin.finch_backend.exception.notfound;

import com.belkin.finch_backend.util.Base62;

public class ImageMetadataNotFoundException extends ResourceNotFoundException {

    public ImageMetadataNotFoundException() {
        super("Image Metadata");
    }

    public ImageMetadataNotFoundException(String id) {
        super("Image Metadata", "Id", id);
    }

    public ImageMetadataNotFoundException(Base62 id) {
        super("Image Metadata", "Id", id.getId());
    }
}
