package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.domain.UserView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public UserView transformToUserView(UserModel userModel) {
        return modelMapper.map(userModel, UserView.class);
    }

    public UserModel transformToUserModel(UserView userView) {
        return modelMapper.map(userView, UserModel.class);
    }
}
