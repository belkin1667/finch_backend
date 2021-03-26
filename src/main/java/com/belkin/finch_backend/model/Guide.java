package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Getter @Setter
public class Guide {

    public static final String DEFAULT_THUMBNAIL_URL = "2l0yHF5D6rW";

    private Base62 id;
    private String authorUsername;
    private String title;
    private String description;
    private String location;
    private OffsetDateTime createdDate;
    private OffsetDateTime travelDate;
    private String thumbnailUrl = DEFAULT_THUMBNAIL_URL;
    private List<String> tags;

    public Guide(String authorUsername, Base62 id, String title, String description, String location, OffsetDateTime createdDate, OffsetDateTime travelDate, String thumbnailUrl, List<String> tags) {
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
        this.tags = tags;
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
        if (newGuide.getTags() != null) {
            setTags(newGuide.getTags());
        }

        log.info("Updated guide: " + new Gson().toJson(this));
    }
}
