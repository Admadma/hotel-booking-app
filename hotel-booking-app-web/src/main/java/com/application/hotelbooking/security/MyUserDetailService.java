package com.application.hotelbooking.security;

import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.transformers.UserViewTransformer;
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

    @Autowired
    private UserViewTransformer userViewTransformer;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userService.getUsersByName(username).size() == 1){
            UserDetails user = new MyUserDetails(userViewTransformer.transformToUserView(userService.getUsersByName(username).get(0)));
            user.getAuthorities().stream().forEach(grantedAuthority -> System.out.println(grantedAuthority.getAuthority()));
            return user;
        } else {
            throw new UsernameNotFoundException("Could not find user with name: " + username);
        }
    }
}
