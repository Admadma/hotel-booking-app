package com.application.hotelbooking.wrappers.implementations;

import com.application.hotelbooking.wrappers.UUIDWrapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDWrapperImpl implements UUIDWrapper {
    @Override
    public UUID getRandomUUID() {
        return UUID.randomUUID();
    }
}
