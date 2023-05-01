package com.application.hotelbooking.services;

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

@Service
public class UserService {

    public static final long DEFAULT_STARTING_VERSION = 1l;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserTransformer userTransformer;
    @Autowired
    private RoleTransformer roleTransformer;

    public List<UserModel> getUsersByName(String username){
        return userTransformer.transformToUserModels(userRepository.findUserByUsername(username)).stream().toList();
    }

    public boolean userExists(String username){
        return getUsersByName(username).size() == 1;
    }

    public void deleteUserByName(String username){
        if (userExists(username)){
            userRepository.delete(userTransformer.transformToUser(getUsersByName(username).get(0)));
        }
    }

    private void save(UserModel userModel){
        userRepository.save(userTransformer.transformToUser(userModel));
    }

    @Transactional
    public void addNewUser(String username, String password, Collection<RoleModel> roles) throws UserAlreadyExistsException{
        if (!userExists(username)){
            UserModel userModel = UserModel.builder()
                    .username(username)
                    .version(DEFAULT_STARTING_VERSION)
                    .password(passwordEncoder.encode(password))
                    .roles(roles)
                    .build();
            save(userModel);
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
        UserModel userModel = getUsersByName(username).get(0);
        if (!userVersionMatches(version, userModel)){
            throw new OptimisticLockException();
        }
        if (!oldPasswordMatches(userModel, oldPassword)){
            throw new CredentialMismatchException("The provided old password does mot match.");
        }

        userModel.setPassword(passwordEncoder.encode(newPassword));
        userModel.setVersion(++version);
        save(userModel);
    }

    private boolean userVersionMatches(Long version, UserModel userModel) {
        return userModel.getVersion().equals(version);
    }

    private boolean oldPasswordMatches(UserModel userModel, String oldPassword){
        return passwordEncoder.matches(oldPassword, userModel.getPassword());
    }
}
