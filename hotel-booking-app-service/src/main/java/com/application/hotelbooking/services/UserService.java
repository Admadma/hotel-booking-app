package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.User;
import com.application.hotelbooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUserByName(String username){
        return userRepository.findUserByUsername(username);
    }

    public void deleteUserByName(String username){
        if (getUserByName(username).size() == 1){
            userRepository.delete(getUserByName(username).get(0));
        }
    }
}
