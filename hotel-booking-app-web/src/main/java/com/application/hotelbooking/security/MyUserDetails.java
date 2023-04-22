package com.application.hotelbooking.security;

import com.application.hotelbooking.domain.RoleView;
import com.application.hotelbooking.domain.UserView;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private UserView userView;

    public MyUserDetails(UserView userView) {
        this.userView = userView;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> roles = getGrantedAuthorities(userView.getRoles());
        return roles;
    }

    @Override
    public String getPassword() {
        return userView.getPassword();
    }

    @Override
    public String getUsername() {
        return userView.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO: look into these
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(Collection<RoleView> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
