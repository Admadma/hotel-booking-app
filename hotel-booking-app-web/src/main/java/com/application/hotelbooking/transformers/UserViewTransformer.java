package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.domain.UserView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

//    public List<UserView> transformToUserViews(List<UserModel> users){
//        return users.stream()
//                .map(user -> modelMapper.map(user, UserView.class))
//                .collect(Collectors.toList());
//    }
}
