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
@Repository("fake_image")
public class FakeImageMetadataDataAccessService implements ImageMetadataDAO {

    List<ImageMetadata> database = new ArrayList<>();

    @Override
    public boolean existsById(Base62 id) {
        log.info("Checking if image metadata with id = " + id.getId() + " is present in database...");
        return database.stream().anyMatch(i -> i.getId().equals(id));
    }

    @Override
    public List<ImageMetadata> findAll() {
        log.info("Reading all images metadata from database...");

        return database;
    }

    @Override
    public Iterable<ImageMetadata> findAllById(Iterable<Base62> base62s) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public ImageMetadata save(ImageMetadata image) {
        log.info("Creating image metadata" + new Gson().toJson(image) + " from database...");

        if (existsById(image.getId()))
            throw new ImageMetadataAlreadyExistsException(image.getId());
        database.add(image);
        return image;
    }

    @Override
    public void deleteById(Base62 id) {
        log.info("Deleting image metadata with id = " + id.getId() + " from database...");

        ImageMetadata image = database.stream().filter(i -> i.getId().equals(id)).findAny()
                .orElseThrow(() -> new ImageMetadataNotFoundException(id));
        database.remove(image);
    }

    @Override
    public void delete(ImageMetadata entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends ImageMetadata> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends ImageMetadata> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ImageMetadata> findById(Base62 id) {
        log.info("Reading all image metadata by id, where id = " + id.getId() + " from database...");
        return database.stream().filter(i -> i.getId().equals(id)).findAny();
    }
}
