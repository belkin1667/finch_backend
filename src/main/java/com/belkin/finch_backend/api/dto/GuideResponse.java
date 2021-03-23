package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.model.Guide;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Getter @Setter
public class GuideResponse {

    private String id;
    private String authorUsername;
    private String title;
    private String description;
    private String location;
    private OffsetDateTime created;
    private OffsetDateTime travelDate;
    private String thumbnailUrl;
    private AccessType type;
    private List<String> tags;
    private Boolean liked = null;
    private Integer likesnum = -1;

    public GuideResponse(Guide guide, AccessType type, Boolean liked, int likesnum) {
        this(guide, type);
        this.liked = liked;
        this.likesnum = likesnum;
    }

    public GuideResponse(Guide guide, AccessType type) {
        log.info("Creating GuideResponse from Guide: " + new Gson().toJson(guide) + " with AccessType = " + type);
        this.id = guide.getId().toString();
        this.thumbnailUrl = Guide.DEFAULT_THUMBNAIL_URL;
        this.type = type;
        this.title = guide.getTitle();
        if (type.equals(AccessType.ME_FULL_ACCESS) || type.equals(AccessType.NOT_ME_FULL_ACCESS)) {
            this.authorUsername = guide.getAuthorUsername();
            this.description = guide.getDescription();
            this.location = guide.getLocation();
            this.created = guide.getCreatedDate();
            this.travelDate = guide.getTravelDate();
            this.tags = guide.getTags();
            this.thumbnailUrl = guide.getThumbnailUrl();
        }

        log.info("Created GuideResponse: " + new Gson().toJson(this));
    }
}
