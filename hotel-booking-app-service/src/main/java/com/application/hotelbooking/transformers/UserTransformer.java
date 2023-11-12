package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.User;
import com.application.hotelbooking.domain.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public User transformToUser(UserModel userModel){
        return modelMapper.map(userModel, User.class);
    }

    public UserModel transformToUserModel(User user){
        return modelMapper.map(user, UserModel.class);
    }

    public Optional<UserModel> transformToOptionalUserModel(Optional<User> user){
        if (user.isPresent()){
            return Optional.of(modelMapper.map(user.get(), UserModel.class));
        } else {
            return Optional.empty();
        }
    }
    public Collection<UserModel> transformToUserModels(Collection<User> users){
        return users.stream()
                .map(user -> modelMapper.map(user, UserModel.class))
                .collect(Collectors.toList());
    }
}
