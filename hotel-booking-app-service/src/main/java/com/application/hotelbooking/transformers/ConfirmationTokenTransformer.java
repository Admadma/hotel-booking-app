package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.ConfirmationToken;
import com.application.hotelbooking.domain.ConfirmationTokenModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public ConfirmationToken transformToConfirmationToken(ConfirmationTokenModel confirmationTokenModel){
        return modelMapper.map(confirmationTokenModel, ConfirmationToken.class);
    }
}
