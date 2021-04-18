package com.belkin.finch_backend.model;

import com.google.gson.Gson;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Slf4j
@Getter @Setter
@AllArgsConstructor @RequiredArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "myuser")
public class User {

    private final String DEFAULT_PROFILE_PHOTO_URL = "";
    private final ProfileAccess DEFAULT_PROFILE_ACCESS = ProfileAccess.ALL;

    @NonNull
    @Column(columnDefinition = "VARCHAR")
    @Id private String username;

    @NonNull
    @Column(columnDefinition = "VARCHAR")
    private String email;

    @Column(columnDefinition = "VARCHAR")
    private String phone;

    @Column(columnDefinition = "VARCHAR")
    private String title;

    @Column(columnDefinition = "VARCHAR")
    private String description;

    @NonNull
    @Column(name = "profile_id", columnDefinition = "VARCHAR")
    private String profilePhotoUrl = DEFAULT_PROFILE_PHOTO_URL;

    @NonNull
    @Column(name = "profile_access")
    private ProfileAccess profileAccess = DEFAULT_PROFILE_ACCESS;

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


