package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.model.User;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class UserResponse {

    private String username;
    private String email;
    private String phone;
    private String title;
    private String description;
    private String profilePhotoUrl;
    private User.ProfileAccess profileAccess;
    private long subscriptionsCount;
    private long subscribersCount;
    private AccessType type;



    public UserResponse(User user, long subscriptionsCount, long subscribersCount, AccessType type) {
        this(user, type, false);
        this.subscribersCount = subscribersCount;
        this.subscriptionsCount = subscriptionsCount;
        log.info("Created UserResponse: " + new Gson().toJson(this));
    }

    private UserResponse(User user, AccessType type, boolean logResult) {
        log.info("Creating UserResponse: " + new Gson().toJson(this));
        this.username = user.getUsername();
        this.title = user.getTitle();
        this.description = user.getDescription();
        this.profilePhotoUrl = user.getProfilePhotoUrl();
        this.profileAccess = user.getProfileAccess();
        this.type = type;

        if (type.equals(AccessType.ME_FULL_ACCESS) || type.equals(AccessType.NOT_ME_FULL_ACCESS)) {
            this.email = user.getEmail();
            this.phone = user.getPhone();
        }
        if (logResult)
            log.info("Created UserResponse: " + new Gson().toJson(this));
    }

    public UserResponse(User user, AccessType type) {
        this(user, type, true);
    }
}
