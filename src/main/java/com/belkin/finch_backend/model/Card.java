package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Card {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public static final String DEFAULT_THUMBNAIL_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg/416px-%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg";

    Base62 id;
    Base62 guideId;
    String thumbnailUrl;
    String title;
    String location;
    String text;
    List<String> tags;

    public Card(Base62 id, Base62 guideId, String thumbnailUrl, String title, String location, String text, List<String> tags) {
        this.id = id;
        this.guideId = guideId;
        this.title = title;
        this.location = location;
        this.text = text;
        this.tags = tags;
        if (thumbnailUrl == null) //TODO: url validator
            this.thumbnailUrl = DEFAULT_THUMBNAIL_URL;
        else
            this.thumbnailUrl = thumbnailUrl;
    }

    public void edit(Card newCard) {
        if (newCard.getId() != null)
            setId(newCard.getId());
        if (newCard.getGuideId() != null)
            setGuideId(newCard.getGuideId());
        if (newCard.getLocation() != null)
            setLocation(newCard.getLocation());
        if (newCard.getTitle() != null)
            setTitle(newCard.getTitle());
        if (newCard.getText() != null)
            setText(newCard.getText());
        if (newCard.getTags() != null)
            setTags(newCard.getTags());
        if (newCard.getThumbnailUrl() != null)
            setThumbnailUrl(newCard.getThumbnailUrl());
    }
}

