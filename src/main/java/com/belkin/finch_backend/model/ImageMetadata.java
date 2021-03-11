package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.io.File;


@Getter @Setter
@AllArgsConstructor
public class ImageMetadata {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public static final Integer ID_LENGTH = 16;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public static final String BASE_PATH = "." + File.separator + "images";

    Base62 id;
    String path;
    String extension = ".jpg";
    String uploaderUsername;

    public ImageMetadata(String uploaderUsername, Base62 id, String extension) {
        this.uploaderUsername = uploaderUsername;
        this.id = id;
        this.extension = extension;
        path = BASE_PATH + "/" + id + extension;
    }

    public ImageMetadata(String uploaderUsername, Base62 id) {
        this.uploaderUsername = uploaderUsername;
        this.id = id;
        path = BASE_PATH + "/" + id + extension;
    }
}
