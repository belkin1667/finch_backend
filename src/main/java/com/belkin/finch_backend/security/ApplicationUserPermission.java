package com.belkin.finch_backend.security;

public enum ApplicationUserPermission {

    //todo: are these permissions correct?
    MODERATORS_PROMOTE("promote:moderator"),
    ADMINS_PROMOTE("promote:admin");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
