package com.application.hotelbooking.transformers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

}
