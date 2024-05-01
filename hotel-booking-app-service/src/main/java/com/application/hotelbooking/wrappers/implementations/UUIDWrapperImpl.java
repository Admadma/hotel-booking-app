package com.application.hotelbooking.wrappers.implementations;

import com.application.hotelbooking.wrappers.UUIDWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDWrapperImpl implements UUIDWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UUIDWrapper.class);

    @Override
    public UUID getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        LOGGER.info("The generated UUID: " + uuid);
        return uuid;
    }
}
