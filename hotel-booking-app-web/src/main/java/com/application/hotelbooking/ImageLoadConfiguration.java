package com.application.hotelbooking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageLoadConfiguration implements WebMvcConfigurer {

    private final String IMAGES_FOLDER_PATH;

    @Autowired
    public ImageLoadConfiguration(@Value("${IMAGES_FOLDER_PATH}") String imagesFolderPath) {
        this.IMAGES_FOLDER_PATH = imagesFolderPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + IMAGES_FOLDER_PATH + "/");
    }
}
