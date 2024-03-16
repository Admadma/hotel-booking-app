package com.application.hotelbooking.security;

import com.application.hotelbooking.domain.RoleView;
import com.application.hotelbooking.domain.UserView;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsTest {

    private static final RoleView ROLE_VIEW = new RoleView(1L, "USER", null);
    private static final List<RoleView> ROLE_VIEW_LIST = List.of(ROLE_VIEW);
    private static final Collection<GrantedAuthority> GRANTED_AUTHORITIES = List.of(new SimpleGrantedAuthority("USER"));
    private static final String TEST_STRING = "test";

    @InjectMocks
    private MyUserDetails myUserDetails;

    @Mock
    private UserView userView;

    @Test
    public void testGetAuthoritiesShouldReturnCollectionOfGrantedAuthoritiesBelongingToUser(){
        when(userView.getRoles()).thenReturn(ROLE_VIEW_LIST);

        Collection<? extends GrantedAuthority> resultGrantedAuthorities = myUserDetails.getAuthorities();

        verify(userView).getRoles();
        Assertions.assertThat(resultGrantedAuthorities).isEqualTo(GRANTED_AUTHORITIES);
    }

    @Test
    public void testGetPasswordShouldReturnPasswordOfUser(){
        when(userView.getPassword()).thenReturn(TEST_STRING);

        String resultPassword = myUserDetails.getPassword();

        verify(userView).getPassword();
        Assertions.assertThat(resultPassword).isEqualTo(TEST_STRING);
    }

    @Test
    public void testGetUsernameShouldReturnUsernameOfUser(){
        when(userView.getUsername()).thenReturn(TEST_STRING);

        String resultUsername = myUserDetails.getUsername();

        verify(userView).getUsername();
        Assertions.assertThat(resultUsername).isEqualTo(TEST_STRING);
    }

    @Test
    public void testIsAccountNonExpiredShouldReturnTrue(){
        boolean resultIsAccountNonExpired = myUserDetails.isAccountNonExpired();

        Assertions.assertThat(resultIsAccountNonExpired).isEqualTo(true);
    }

    @Test
    public void testIsAccountNonLockedShouldReturnTrueIfAccountIsNotLocked(){
        when(userView.getLocked()).thenReturn(false);

        boolean resultIsAccountNonLocked = myUserDetails.isAccountNonLocked();

        verify(userView).getLocked();
        Assertions.assertThat(resultIsAccountNonLocked).isEqualTo(true);
    }

    @Test
    public void testIsCredentialsNonExpiredShouldReturnTrue(){
        boolean resultIsCredentialsNonExpired = myUserDetails.isCredentialsNonExpired();

        Assertions.assertThat(resultIsCredentialsNonExpired).isEqualTo(true);
    }

    @Test
    public void testIsEnabledShouldReturnTrueIfAccountIsEnabled(){
        when(userView.getEnabled()).thenReturn(true);

        boolean resultIsAccountNonLocked = myUserDetails.isEnabled();

        verify(userView).getEnabled();
        Assertions.assertThat(resultIsAccountNonLocked).isEqualTo(true);
    }
}
