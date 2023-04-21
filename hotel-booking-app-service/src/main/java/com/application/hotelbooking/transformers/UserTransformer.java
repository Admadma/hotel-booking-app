package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.domain.User;
import com.application.hotelbooking.domain.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public UserModel transformToModel(User user){
        return modelMapper.map(user, UserModel.class);
    }

    public User transformToRole(UserModel userModel){
        return modelMapper.map(userModel, User.class);
    }
}
