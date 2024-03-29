package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.util.Base62;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter @Setter
public class GuideRequest {

    private Base62 id;
    private String title;
    private String description;
    private String location;
    private String travelDate;
    private String thumbnailUrl;
    private List<String> tags;
}
