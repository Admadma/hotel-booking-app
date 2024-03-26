package com.application.hotelbooking;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageLoadConfiguration implements WebMvcConfigurer {

    private final String IMAGES_FOLDER_PATH;

    @Autowired
    public ImageLoadConfiguration(Dotenv dotenv) {
        this.IMAGES_FOLDER_PATH = dotenv.get("IMAGES_FOLDER_PATH");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("file:" + IMAGES_FOLDER_PATH + "/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + IMAGES_FOLDER_PATH + "/");
    }
}
