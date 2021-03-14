package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.ImageMetadataDAO;
import com.belkin.finch_backend.exception.alreadyexist.ImageMetadataAlreadyExistsException;
import com.belkin.finch_backend.exception.notfound.ImageMetadataNotFoundException;
import com.belkin.finch_backend.model.ImageMetadata;
import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("image_fake")
public class FakeImageMetadataDataAccessService implements ImageMetadataDAO {

    List<ImageMetadata> database = new ArrayList<>();

    @Override
    public boolean isPresent(Base62 id) {
        log.info("Checking if image metadata with id = " + id.getId() + " is present in database...");
        return database.stream().anyMatch(i -> i.getId().equals(id));
    }

    @Override
    public List<ImageMetadata> getAllImages() {
        log.info("Reading all images metadata from database...");

        return database;
    }

    @Override
    public void addImage(ImageMetadata image) {
        log.info("Creating image metadata" + new Gson().toJson(image) + " from database...");

        if (isPresent(image.getId()))
            throw new ImageMetadataAlreadyExistsException(image.getId());
        database.add(image);
    }

    @Override
    public boolean deleteImage(Base62 id) {
        log.info("Deleting image metadata with id = " + id.getId() + " from database...");

        ImageMetadata image = database.stream().filter(i -> i.getId().equals(id)).findAny()
                .orElseThrow(() -> new ImageMetadataNotFoundException(id));
        return database.remove(image);
    }

    @Override
    public Optional<ImageMetadata> getImageById(Base62 id) {
        log.info("Reading all image metadata by id, where id = " + id.getId() + " from database...");
        return database.stream().filter(i -> i.getId().equals(id)).findAny();
    }
}
