package com.belkin.finch_backend.model;

import com.google.gson.Gson;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
@AllArgsConstructor @RequiredArgsConstructor
public class User {

    private final String DEFAULT_PROFILE_PHOTO_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg/416px-%D0%9F%D1%83%D1%82%D0%B8%D0%BD_23.12.20.jpg";
    private final ProfileAccess DEFAULT_PROFILE_ACCESS = ProfileAccess.ALL;

    @NonNull private String username;
    @NonNull private String email;
    private String phone;
    private String title;
    private String description;
    @NonNull private String profilePhotoUrl = DEFAULT_PROFILE_PHOTO_URL;
    @NonNull private ProfileAccess profileAccess = DEFAULT_PROFILE_ACCESS;

    public User(String username) {
        log.info("Creating user...");

        this.username = username;

        log.info("Created User: " + new Gson().toJson(this));
    }

    public enum ProfileAccess {
        ALL,
        NONE,
        MUTUAL_FOLLOWERS
    }
}


