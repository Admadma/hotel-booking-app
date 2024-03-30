package com.application.hotelbooking.services.imagehandling;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.application.hotelbooking.wrappers.FilesWrapper;
import com.application.hotelbooking.wrappers.UUIDWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    private UUIDWrapper uuidWrapper;

    private FilesWrapper filesWrapper;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, UUIDWrapper uuidWrapper, FilesWrapper filesWrapper) {

        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        this.uuidWrapper = uuidWrapper;
        this.filesWrapper = filesWrapper;
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            if (!file.getOriginalFilename().toLowerCase().matches(".*\\.(jpg|jpeg|png)$")) {
                throw new StorageException("Not an accepted file format.");
            }

            String newName = uuidWrapper.getRandomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
            Path destinationFile = this.rootLocation
                    .resolve(Paths.get(newName))
                    .normalize()
                    .toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                filesWrapper.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                return newName;
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }
}
