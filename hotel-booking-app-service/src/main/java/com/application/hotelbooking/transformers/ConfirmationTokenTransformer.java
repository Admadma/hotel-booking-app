package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.ConfirmationToken;
import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.ReservationModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfirmationTokenTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public ConfirmationToken transformToConfirmationToken(ConfirmationTokenModel confirmationTokenModel){
        return modelMapper.map(confirmationTokenModel, ConfirmationToken.class);
    }

    public ConfirmationTokenModel transformToConfirmationTokenModel(ConfirmationToken confirmationToken){
        return modelMapper.map(confirmationToken, ConfirmationTokenModel.class);
    }

    public Optional<ConfirmationTokenModel> transformToOptionalConfirmationTokenModel(Optional<ConfirmationToken> confirmationToken){
        if (confirmationToken.isPresent()){
            return Optional.of(modelMapper.map(confirmationToken, ConfirmationTokenModel.class));
        } else {
            return Optional.empty();
        }
    }
}
