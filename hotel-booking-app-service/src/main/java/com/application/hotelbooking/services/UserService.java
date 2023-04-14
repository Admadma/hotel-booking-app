package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.User;
import com.application.hotelbooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // TODO: findUserByUsername returns a list of users. Using it like this can lead to errors later
    public User getUserByName(String username){
        return userRepository.findUserByUsername(username).get(0);
    }
}
