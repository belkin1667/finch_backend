package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Getter @Setter
@Entity
@NoArgsConstructor
public class Guide {

    public static final String DEFAULT_THUMBNAIL_URL = "";

    @EmbeddedId
    private Base62 id;

    @Column(name = "author", columnDefinition = "VARCHAR")
    private String authorUsername;

    @Column(columnDefinition = "VARCHAR")
    private String title;

    @Column(columnDefinition = "VARCHAR")
    private String description;

    @Column(columnDefinition = "VARCHAR")
    private String location;

    @Column(name = "created_date")
    private OffsetDateTime createdDate;

    @Column(name = "travel_date")
    private OffsetDateTime travelDate;

    @Column(name = "thumbnail_id", columnDefinition = "VARCHAR")
    private String thumbnailUrl = DEFAULT_THUMBNAIL_URL;

    @ElementCollection
    private List<String> tags;

    /**
     * Converts to string to format "yyyy-MM-ddThh:mm:ss+01:00"
     * @param datetime String in format "yyyy-MM-ddThh:mm:ss.sssZ"
     * @return Formatted Datetime
     */
    private static OffsetDateTime parseDate(String datetime) {
        if (datetime == null || datetime.isEmpty() || datetime.isBlank())
            return null;

        int index = datetime.lastIndexOf('.');
        String travelDateString = datetime.substring(0, index) + "+01:00";
        return OffsetDateTime.parse(travelDateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

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

    public Guide(String authorUsername, Base62 id, String title, String description, String location, OffsetDateTime createdDate, String travelDate, String thumbnailUrl, List<String> tags) {
        this(authorUsername, id, title, description, location, createdDate, parseDate(travelDate), thumbnailUrl, tags);
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
