package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.ConfirmationToken;
import com.application.hotelbooking.domain.ConfirmationTokenModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ConfirmationTokenTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public ConfirmationToken transformToConfirmationToken(ConfirmationTokenModel confirmationTokenModel){
        return modelMapper.map(confirmationTokenModel, ConfirmationToken.class);
    }

    public Optional<ConfirmationTokenModel> transformToConfirmationTokenModel(Optional<ConfirmationToken> confirmationToken){
        if (confirmationToken.isPresent()){
            return Optional.of(modelMapper.map(confirmationToken.get(), ConfirmationTokenModel.class));
        } else {
            return Optional.empty();
        }
    }
}
