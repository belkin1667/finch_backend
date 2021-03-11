package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.util.Base62;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class GuideRequest {

    private Base62 id;
    @NonNull private String title;
    @NonNull private String description;
    @NonNull private Date created;
    private String thumbnailUrl;

}
