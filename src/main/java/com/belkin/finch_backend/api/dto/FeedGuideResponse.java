package com.belkin.finch_backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class FeedGuideResponse {
    String id;
    String username;
    String profilePhotoUrl;
}
