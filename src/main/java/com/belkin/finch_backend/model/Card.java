package com.belkin.finch_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
public class Card {

    private final String DEFAULT_THUMBNAIL_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg/416px-%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg";

    UUID id;
    UUID guideId;
    @NonNull String thumbnailUrl = DEFAULT_THUMBNAIL_URL;
    String title;
    String location;
    String text;
    List<String> tags;
}
