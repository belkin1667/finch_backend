package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter @Setter
public class Card {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public static final String DEFAULT_THUMBNAIL_URL = "2l0yHF5D6rW";

    Base62 id;
    Base62 guideId;
    String thumbnailUrl;
    String title;
    String location;
    List<Content> content;

    public Card(Base62 id, Base62 guideId, String thumbnailUrl, String title, String location, List<Content> content) {
        log.info("Creating Card...");

        this.id = id;
        this.guideId = guideId;
        this.title = title;
        this.location = location;
        this.content = content;
        if (thumbnailUrl == null) //TODO: url validator
            this.thumbnailUrl = DEFAULT_THUMBNAIL_URL;
        else
            this.thumbnailUrl = thumbnailUrl;

        log.info("Created card instance: " + new Gson().toJson(this));
    }

    public void edit(Card newCard) {
        log.info("Updating card instance... Old value: " + new Gson().toJson(this) + " Updated fields: " + new Gson().toJson(newCard));

        if (newCard.getId() != null)
            setId(newCard.getId());
        if (newCard.getGuideId() != null)
            setGuideId(newCard.getGuideId());
        if (newCard.getLocation() != null)
            setLocation(newCard.getLocation());
        if (newCard.getTitle() != null)
            setTitle(newCard.getTitle());
        if (newCard.getContent() != null)
            setContent(newCard.getContent());
        if (newCard.getThumbnailUrl() != null)
            setThumbnailUrl(newCard.getThumbnailUrl());

        log.info("Updated card: " + new Gson().toJson(this));
    }
}

