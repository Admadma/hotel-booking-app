package com.application.hotelbooking.security;

import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
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
    private UserRepositoryService userRepositoryService;

    @Autowired
    private UserViewTransformer userViewTransformer;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepositoryService.getUserByName(username).isPresent()){
            UserDetails user = new MyUserDetails(userViewTransformer.transformToUserView(userRepositoryService.getUserByName(username).get()));
            return user;
        } else {
            throw new UsernameNotFoundException("Could not find user with name: " + username);
        }
    }
}
