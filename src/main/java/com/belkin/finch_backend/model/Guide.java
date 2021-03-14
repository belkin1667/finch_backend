package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.Date;

@Slf4j
@Getter @Setter
public class Guide {

    public static final String DEFAULT_THUMBNAIL_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg/416px-%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg";

    private Base62 id;
    private String authorUsername;
    private String title;
    private String description;
    private String location;
    private OffsetDateTime createdDate;
    private OffsetDateTime travelDate;
    private String thumbnailUrl = DEFAULT_THUMBNAIL_URL;

    public Guide(String authorUsername, Base62 id, String title, String description, String location, OffsetDateTime createdDate, OffsetDateTime travelDate, String thumbnailUrl) {
        log.info("Creating Guide...");

        this.authorUsername = authorUsername;
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.createdDate = createdDate;
        this.travelDate = travelDate;
        if (thumbnailUrl == null) //TODO: url validator
            this.thumbnailUrl = DEFAULT_THUMBNAIL_URL;
        else
            this.thumbnailUrl = thumbnailUrl;

        log.info("Created guide instance: " + new Gson().toJson(this));
    }

    public void edit(Guide newGuide) {
        log.info("Updating guide instance... Old value: " + new Gson().toJson(this) + " Updated fields: " + new Gson().toJson(newGuide));

        if (newGuide.getAuthorUsername() != null)
            setAuthorUsername(newGuide.getAuthorUsername());
        if (newGuide.getId() != null)
            setId(newGuide.getId());
        if (newGuide.getTitle() != null)
            setTitle(newGuide.getTitle());
        if (newGuide.getDescription() != null)
            setDescription(newGuide.getDescription());
        if (newGuide.getLocation() != null)
            setLocation(newGuide.getLocation());
        if (newGuide.getCreatedDate() != null)
            setCreatedDate(newGuide.getCreatedDate());
        if (newGuide.getTravelDate() != null)
            setTravelDate(newGuide.getTravelDate());
        if (newGuide.getThumbnailUrl() != null)
            setThumbnailUrl(newGuide.getThumbnailUrl());

        log.info("Updated guide: " + new Gson().toJson(this));
    }
}
