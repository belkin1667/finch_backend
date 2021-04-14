package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.File;

@Slf4j
@Getter @Setter
@Entity
@AllArgsConstructor @NoArgsConstructor
public class ImageMetadata {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public static final Integer ID_LENGTH = 16;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public static final String BASE_PATH = "." + File.separator + "images";

    @EmbeddedId
    Base62 id;

    @Column(columnDefinition = "VARCHAR")
    String path;

    @Column(columnDefinition = "VARCHAR")
    String extension = ".jpg";

    @Column(name = "uploader", columnDefinition = "VARCHAR")
    String uploaderUsername;

    public ImageMetadata(String uploaderUsername, Base62 id, String extension) {
        log.info("Creating image metadata...");

        this.uploaderUsername = uploaderUsername;
        this.id = id;
        this.extension = extension;
        path = BASE_PATH + "/" + id + extension;

        log.info("Created image metadata: " + new Gson().toJson(this));
    }

    public ImageMetadata(String uploaderUsername, Base62 id) {
        log.info("Creating image metadata...");

        this.uploaderUsername = uploaderUsername;
        this.id = id;
        path = BASE_PATH + "/" + id + extension;

        log.info("Created image metadata: " + new Gson().toJson(this));
    }
}
