package com.arete.korbly.infrastructure.security;

import com.arete.korbly.modules.shared.domain.AppUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class UserPrincipal implements UserDetails {
    private final Collection<? extends GrantedAuthority> authorities;
    private final AppUser appUser;

    public UserPrincipal(Collection<? extends GrantedAuthority> authorities, AppUser appUser) {
        this.authorities = authorities;
        this.appUser = appUser;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    public UUID getAppUserId(){
        return appUser.getUserId();
    }

    @Override
    public String getUsername() {
        return appUser.getPrimaryContactEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
