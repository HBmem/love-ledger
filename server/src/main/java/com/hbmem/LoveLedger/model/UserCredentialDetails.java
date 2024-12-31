package com.hbmem.LoveLedger.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserCredentialDetails implements UserDetails {
    private final UserCredential userCredential;

    public UserCredentialDetails(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO: Implement user account expiration
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO: Implement user locked or unlocked status
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO: Implement password credential expiration
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !userCredential.isDisabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userCredential.getPassword();
    }

    @Override
    public String getUsername() {
        return userCredential.getUsername();
    }
}
