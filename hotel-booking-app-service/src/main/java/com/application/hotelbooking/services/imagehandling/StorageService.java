package com.application.hotelbooking.services.imagehandling;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(MultipartFile file);
}