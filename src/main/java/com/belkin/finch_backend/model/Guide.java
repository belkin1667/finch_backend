package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import lombok.*;
import java.util.Date;

@Getter @Setter
@AllArgsConstructor @RequiredArgsConstructor
public class Guide {

    public static final String DEFAULT_THUMBNAIL_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg/416px-%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg";

    private Base62 id;
    private String authorUsername;
    private String title;
    private String description;
    private Date created;
    @NonNull private String thumbnailUrl = DEFAULT_THUMBNAIL_URL;

    public Guide(String authorUsername, Base62 id, String title, String description, Date created, String thumbnailUrl) {
        this.authorUsername = authorUsername;
        this.id = id;
        this.title = title;
        this.description = description;
        this.created = created;
        if (thumbnailUrl != null) //TODO: url validator
            this.thumbnailUrl = thumbnailUrl;
    }
}
