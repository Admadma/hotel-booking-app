package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.domain.User;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.repositories.UserRepository;
import com.application.hotelbooking.transformers.RoleTransformer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleTransformer roleTransformer;

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

    @Transactional
    public void addNewUser(String username, String password, Collection<RoleModel> roles) throws UserAlreadyExistsException{
        System.out.println("-----------------");
        if (!userExists(username)){
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(roleTransformer.transformToRoles(roles));
            System.out.println("saving");
            userRepository.save(user);
            System.out.println("saved");
        } else {
            throw new UserAlreadyExistsException("That username is taken.");
        }
    }

    @Transactional
    public void createAdminUserIfNotFound(String username, String password, Collection<RoleModel> roles){
        if (userRepository.findUserByUsername(username).isEmpty()){
            addNewUser(username, password, roles);
        }
    }
}
