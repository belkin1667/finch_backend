package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.ImageMetadata;
import com.belkin.finch_backend.util.Base62;

import java.util.List;
import java.util.Optional;

public interface ImageMetadataDAO {

    boolean isPresent(Base62 id);

    List<ImageMetadata> getAllImages();

    void addImage(ImageMetadata image);

    boolean deleteImage(Base62 id);

    Optional<ImageMetadata> getImageById(Base62 id);
}
