package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.User;
import com.application.hotelbooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUserByName(String username){
        return userRepository.findUserByUsername(username);
    }

    public boolean userExists(String username){
        // This also fails if there are more than one match for the given username
        // TODO: UNIQUE restraint on username in database and handle user registration
        return getUserByName(username).size() == 1;
    }

    public void deleteUserByName(String username){
        if (userExists(username)){
            userRepository.delete(getUserByName(username).get(0));
        }
    }

    public void addNewUser(String username, String password){
        User user = new User(username, passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
