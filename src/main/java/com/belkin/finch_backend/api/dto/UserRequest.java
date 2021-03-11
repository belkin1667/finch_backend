package com.belkin.finch_backend.api.dto;

import com.belkin.finch_backend.model.User;
import com.belkin.finch_backend.security.model.ApplicationUser;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private String phone;
    private String title;
    private String description;
    private String profilePhotoUrl;
    private User.ProfileAccess profileAccess;

    public User getUser(User oldUser) {
        if (username != null)
            oldUser.setUsername(username);
        if (email != null)
            oldUser.setEmail(email);
        if (phone != null)
            oldUser.setPhone(phone);
        if(title != null)
            oldUser.setTitle(title);
        if (description != null)
            oldUser.setDescription(description);
        if (profilePhotoUrl != null)
            oldUser.setProfilePhotoUrl(profilePhotoUrl);
        if(profileAccess != null)
            oldUser.setProfileAccess(profileAccess);

        return oldUser;
    }

    public ApplicationUser getApplicationUser(ApplicationUser oldUser) {
        if (username != null)
            oldUser.setUsername(username);
        if (email != null)
            oldUser.setEmail(email);
        if (password != null)
            oldUser.setEmail(password); //TODO: is password changing works fine?
        return oldUser;
    }
}
