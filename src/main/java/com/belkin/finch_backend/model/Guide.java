package com.belkin.finch_backend.model;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor @RequiredArgsConstructor
public class Guide {
    private final String DEFAULT_THUMBNAIL_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg/416px-%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg";

    private UUID id;
    private String title;
    private String description;
    private Date created;
    @NonNull private String thumbnailUrl = DEFAULT_THUMBNAIL_URL;
    private String authorUsername;

}
