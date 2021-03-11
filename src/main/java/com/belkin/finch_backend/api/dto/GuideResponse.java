package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.model.Guide;
import com.belkin.finch_backend.util.Base62;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class GuideResponse {

    private String id;
    private String authorUsername;
    private String title;
    private String description;
    private Date created;
    private String thumbnailUrl;
    private AccessType type;

    public GuideResponse(Guide guide, AccessType type) {
        this.id = guide.getId().toString();
        this.thumbnailUrl = Guide.DEFAULT_THUMBNAIL_URL;
        this.type = type;
        this.title = guide.getTitle();
        if (type.equals(AccessType.ME_FULL_ACCESS) || type.equals(AccessType.NOT_ME_FULL_ACCESS)) {
            this.authorUsername = guide.getAuthorUsername();
            this.description = guide.getDescription();
            this.created = guide.getCreated();
            this.thumbnailUrl = guide.getThumbnailUrl();
        }
    }
}
