package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.CredentialMismatchException;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.repositories.UserRepository;
import com.application.hotelbooking.transformers.RoleTransformer;
import com.application.hotelbooking.transformers.UserTransformer;
import jakarta.persistence.OptimisticLockException;
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
    private UserTransformer userTransformer;
    @Autowired
    private RoleTransformer roleTransformer;

    public List<UserModel> getUserByName(String username){
        // TODO: either make it return only one, or rename the method
        return userTransformer.transformToUserModels(userRepository.findUserByUsername(username)).stream().toList();
    }

    public boolean userExists(String username){
        // This also fails if there are more than one match for the given username
        // TODO: UNIQUE restraint on username in database and handle user registration
        return getUserByName(username).size() == 1;
    }

    public void deleteUserByName(String username){
        if (userExists(username)){
            userRepository.delete(userTransformer.transformToUser(getUserByName(username).get(0)));
        }
    }

    private void save(UserModel userModel){
        userRepository.save(userTransformer.transformToUser(userModel));
    }

    @Transactional
    public void addNewUser(String username, String password, Collection<RoleModel> roles) throws UserAlreadyExistsException{
        System.out.println("-----------------");
        if (!userExists(username)){
            UserModel userModel = new UserModel();
            userModel.setUsername(username);
            userModel.setVersion(1l);
            userModel.setPassword(passwordEncoder.encode(password));
            userModel.setRoles(roles);

            System.out.println("saving");
            save(userModel);
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

    public void changePassword(String username, String newPassword, String oldPassword, Long version) throws OptimisticLockException{
        UserModel user = getUserByName(username).get(0);
        if (!userVersionMatches(version, user)){
            throw new OptimisticLockException();
        }
        if (!oldPasswordMatches(user, oldPassword)){
            throw new CredentialMismatchException("The provided old password does mot match.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVersion(++version);
        save(user);
    }

    private boolean userVersionMatches(Long version, UserModel user) {
        return user.getVersion().equals(version);
    }

    private boolean oldPasswordMatches(UserModel userModel, String oldPassword){
        return passwordEncoder.matches(oldPassword, userModel.getPassword());
    }
}
