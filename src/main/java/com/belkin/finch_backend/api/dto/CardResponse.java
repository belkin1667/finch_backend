package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.model.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CardResponse {

    private String id;
    private String guideId;
    private String thumbnailUrl = Card.DEFAULT_THUMBNAIL_URL;
    private String title;
    private String location;
    private String text;
    private List<String> tags;
    private AccessType accessType;

    public CardResponse(Card card, AccessType type) {
        this.id = card.getId().toString();
        this.guideId = card.getGuideId().toString();
        this.title = card.getTitle();
        if (type.equals(AccessType.ME_FULL_ACCESS) || type.equals(AccessType.NOT_ME_FULL_ACCESS)) {
            this.location = card.getLocation();
            this.text = card.getText();
            this.tags = card.getTags();
            this.thumbnailUrl = card.getThumbnailUrl();
        }
    }
}
