package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class UserResponse {

    private String username;
    private String email;
    private String phone;
    private String title;
    private String description;
    private String profilePhotoUrl;
    private User.ProfileAccess profileAccess;
    private Integer subscriptionsCount;
    private Integer subscribersCount;
    private AccessType type;



    public UserResponse(User user, Integer subscriptionsCount, Integer subscribersCount, AccessType type) {
        this(user, type);
        this.subscribersCount = subscribersCount;
        this.subscriptionsCount = subscriptionsCount;
    }


    public UserResponse(User user, AccessType type) {
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
    }
}
