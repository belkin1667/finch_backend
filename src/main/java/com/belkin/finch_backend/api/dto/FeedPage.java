package com.belkin.finch_backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class FeedPage {

    int page;
    List<FeedGuideResponse> guides;
}
