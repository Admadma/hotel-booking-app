package com.application.hotelbooking.security;

import com.application.hotelbooking.services.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userService.getUserByName(username).size() == 1){
            UserDetails user = new MyUserDetails(userService.getUserByName(username).get(0)); // TODO: transform to UserView before passing to MyUserDetails
            user.getAuthorities().stream().forEach(grantedAuthority -> System.out.println(grantedAuthority.getAuthority()));
            return user;
        } else {
            throw new UsernameNotFoundException("Could not find user with name: " + username);
        }
    }
}
