package com.application.hotelbooking.transformers;

import com.application.hotelbooking.views.UserView;
import com.application.hotelbooking.models.UserModel;
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
}
