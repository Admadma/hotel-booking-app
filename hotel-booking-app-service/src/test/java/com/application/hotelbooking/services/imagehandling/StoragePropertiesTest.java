package com.application.hotelbooking.services.imagehandling;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoragePropertiesTest {

    private static final String LOCATION = "locationpath";
    private static final String NEW_LOCATION = "locationpath";

    @Mock
    private Dotenv dotenv;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetLocationShouldReturnTheStored(){
        when(dotenv.get("IMAGES_FOLDER_PATH")).thenReturn(LOCATION);

        StorageProperties storageProperties = new StorageProperties(dotenv);

        verify(dotenv).get("IMAGES_FOLDER_PATH");
        Assertions.assertEquals(storageProperties.getLocation(), LOCATION);
    }

    @Test
    void testSetLocationShouldUpdateTheLocation(){
        when(dotenv.get("IMAGES_FOLDER_PATH")).thenReturn(LOCATION);

        StorageProperties storageProperties = new StorageProperties(dotenv);
        storageProperties.setLocation(NEW_LOCATION);

        verify(dotenv).get("IMAGES_FOLDER_PATH");
        Assertions.assertEquals(storageProperties.getLocation(), NEW_LOCATION);
    }
}
