package com.belkin.finch_backend.security.model;

import com.belkin.finch_backend.security.ApplicationUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
public class ApplicationUser implements UserDetails {

    public ApplicationUser(String username, String email, String password, Set<ApplicationUserRole> roles, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles.stream().map(Enum::name).collect(Collectors.toSet());
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    @Setter
    @Id
    @Column(columnDefinition = "VARCHAR")
    private String username;

    @Getter @Setter
    @Column(columnDefinition = "VARCHAR")
    private String email;

    @Column(columnDefinition = "VARCHAR")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @Column(name = "account_expired")
    private boolean isAccountNonExpired;

    @Column(name = "account_locked")
    private boolean isAccountNonLocked;

    @Column(name = "cred_non_expired")
    private boolean isCredentialsNonExpired;

    @Column(name = "enabled")
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authList = new HashSet<>();

        for (String role : roles) {
            authList.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return authList;
    }

    public void setApplicationUserRoles(Set<ApplicationUserRole> roles) {
        this.roles = roles.stream().map(Enum::name).collect(Collectors.toSet());
    }

    public Set<ApplicationUserRole> getApplicationUserRoles() {
        Set<ApplicationUserRole> appuserRoles = new HashSet<>();
        return roles.stream().map(ApplicationUserRole::valueOf).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


}
