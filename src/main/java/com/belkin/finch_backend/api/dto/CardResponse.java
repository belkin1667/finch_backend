package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.model.Card;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter @Setter
public class CardResponse {

    private String id;
    private String guideId;
    private String thumbnailUrl = Card.DEFAULT_THUMBNAIL_URL;
    private String title;
    private String location;
    private String content;
    private List<String> tags;
    private AccessType accessType;

    public CardResponse(Card card, AccessType type) {
        log.info("Creating CardResponse from Card: " + new Gson().toJson(card) + " with AccessType = " + type);

        this.id = card.getId().toString();
        this.guideId = card.getGuideId().toString();
        this.title = card.getTitle();
        if (type.equals(AccessType.ME_FULL_ACCESS) || type.equals(AccessType.NOT_ME_FULL_ACCESS)) {
            this.location = card.getLocation();
            this.content = card.getText();
            this.tags = card.getTags();
            this.thumbnailUrl = card.getThumbnailUrl();
        }

        log.info("Created CardResponse: " + new Gson().toJson(this));
    }
}
