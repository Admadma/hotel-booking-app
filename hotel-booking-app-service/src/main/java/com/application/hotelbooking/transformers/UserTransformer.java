package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.User;
import com.application.hotelbooking.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public User transformToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    public Collection<UserDTO> transformToUserDTOs(Collection<User> users){
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }
}
