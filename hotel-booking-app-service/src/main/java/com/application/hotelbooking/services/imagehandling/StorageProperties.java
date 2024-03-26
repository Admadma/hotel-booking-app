package com.application.hotelbooking.services.imagehandling;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
    private String location;

    @Autowired
    public StorageProperties(Dotenv dotenv) {
        this.location = dotenv.get("IMAGES_FOLDER_PATH");
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}