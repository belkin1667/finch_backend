package com.belkin.finch_backend.dto;

import com.belkin.finch_backend.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class UserProfileDTO {

    private String username;
    private String email;
    private String phone;
    private String title;
    private String description;
    private String profilePhotoUrl;
    private User.ProfileAccess profileAccess;
    private Set<String> subscriptions;
    private Set<String> subscribers;
    private Integer subscriptionsCount;
    private Integer subscribersCount;
    private boolean isMe;

    public enum UserType {
        ME,
        PARTIAL_ACCESS,
        FULL_ACCESS
    }

    public static UserProfileDTO getUserProfileDTO(User user, UserType type) {
        UserProfileDTO userProfileDTO = null;
        switch (type) {
            case ME:
                userProfileDTO = new UserProfileDTO(user, true, true);
                break;
            case PARTIAL_ACCESS:
                userProfileDTO = new UserProfileDTO(user, false, false);
                break;
            case FULL_ACCESS:
                userProfileDTO = new UserProfileDTO(user, false, true);
                break;
        }
        return userProfileDTO;
    }

    private UserProfileDTO(User user, boolean isMe, boolean fullAccess) {
        this.username = user.getUsername();
        this.title = user.getTitle();
        this.description = user.getDescription();
        this.profilePhotoUrl = user.getProfilePhotoUrl();
        this.profileAccess = user.getProfileAccess();
        this.isMe = isMe;
        this.subscribersCount = user.getSubscribers().size();
        this.subscriptionsCount = user.getSubscriptions().size();

        if (isMe || fullAccess) {
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.subscribers = user.getSubscribers();
            this.subscriptions = user.getSubscriptions();
        }
    }
}
