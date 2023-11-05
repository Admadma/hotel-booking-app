package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.UserView;
import com.application.hotelbooking.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public UserView transformToUserView(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserView.class);
    }
}
