package com.belkin.finch_backend.api.dto;


import com.belkin.finch_backend.util.Base62;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CardRequest {
    private Base62 id;
    private Base62 guideId;
    String thumbnailUrl;
    String title;
    String location;
    String content;
}
