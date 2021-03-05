package com.belkin.finch_backend.model;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @NonNull private Set<String> subscriptions = new HashSet<>();
    @NonNull private Set<String> subscribers = new HashSet<>();

    public User(String username) {
        this.username = username;
    }

    public enum ProfileAccess {
        ALL,
        NONE,
        MUTUAL_FOLLOWERS;
    }
}


