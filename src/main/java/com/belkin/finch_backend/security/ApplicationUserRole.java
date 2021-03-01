package com.belkin.finch_backend.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.belkin.finch_backend.security.ApplicationUserPermission.ADMINS_PROMOTE;
import static com.belkin.finch_backend.security.ApplicationUserPermission.MODERATORS_PROMOTE;

public enum ApplicationUserRole {
    USER(Sets.newHashSet()),
    MODERATOR(Sets.newHashSet(MODERATORS_PROMOTE)),
    ADMIN(Sets.newHashSet(MODERATORS_PROMOTE, ADMINS_PROMOTE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) { this.permissions = permissions; }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<? extends GrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> grantedAuthorities = getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
